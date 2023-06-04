package test

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.network.tls.*
import mu.KotlinLogging
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyPairGeneratorSpi
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import spark.Spark.*
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.Instant
import java.util.*
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.collections.Map
import kotlin.concurrent.thread

fun generateCert(commonName: String): Pair<KeyPair, X509Certificate> {
    val keygen = KeyPairGeneratorSpi()
    keygen.initialize(2048)
    val keypair = keygen.generateKeyPair()!!

    val owner = X500Name("CN=$commonName")
    val serial = BigInteger(64, SecureRandom())
    val notBefore = Date.from(Instant.now())
    val notAfter = Date.from(Instant.now().plusSeconds(24 * 60 * 60))
    val pubKeyInfo = SubjectPublicKeyInfo.getInstance(keypair.public.encoded)

    val builder = X509v3CertificateBuilder(owner, serial, notBefore, notAfter, owner, pubKeyInfo)

    val signer = JcaContentSignerBuilder("SHA256withRSA").build(keypair.private)
    val certHolder = builder.build(signer)

    val cert = JcaX509CertificateConverter().getCertificate(certHolder)
    cert.verify(keypair.public)

    return keypair to cert
}

class SslSettings(
    dir: String,
    name: String,
    val passwordKS: String,
    val passwordTS: String,
    val keyPair: KeyPair,
    val cert: X509Certificate,
    trustedCerts: Map<String, X509Certificate>
) {

    val pathKS = "$dir/ks_$name.p12"
    val pathTS = "$dir/ts_$name.p12"

    val keyStore: KeyStore = KeyStore.getInstance("PKCS12")
    val trustStore: KeyStore = KeyStore.getInstance("PKCS12")


    init {
        keyStore.load(null, null)
        keyStore.setKeyEntry("", keyPair.private, passwordKS.toCharArray(), arrayOf(cert))
        keyStore.store(FileOutputStream(pathKS), passwordKS.toCharArray())

        trustStore.load(null, null)
        trustedCerts.forEach { (n, c) ->
            trustStore.setCertificateEntry(n, c)
        }
        trustStore.store(FileOutputStream(pathTS), passwordTS.toCharArray())
    }

    private fun getTrustManagerFactory(): TrustManagerFactory? {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)
        return trustManagerFactory
    }

    fun getTrustManager(): X509TrustManager {
        return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
    }
}

fun server(keyPair: KeyPair, certificate: X509Certificate, clientCertificate: X509Certificate) {
    val keystorePassword = "password"
    val truststorePassword = "password"

    val sslSettings = SslSettings(
        "/tmp",
        "server",
        keystorePassword,
        truststorePassword,
        keyPair,
        certificate,
        mapOf("client" to clientCertificate)
    )

    port(3060)
    with(sslSettings) {
        secure(pathKS, passwordKS, pathTS, passwordTS, true)
    }

    get("/hello") { _, _ ->
        "Hello World"
    }
}

suspend fun client(keyPair: KeyPair, certificate: X509Certificate, serverCertificate: X509Certificate) {
    val keystorePassword = "password"
    val truststorePassword = "password"

    val sslSettings = SslSettings(
        "/tmp",
        "client",
        keystorePassword,
        truststorePassword,
        keyPair,
        certificate,
        mapOf("server" to serverCertificate)
    )
    val client = HttpClient(CIO) {
        engine {
            https {
                trustManager = sslSettings.getTrustManager()
                certificates.add(CertificateAndKey(arrayOf(sslSettings.cert), sslSettings.keyPair.private))
            }
        }
    }
    val response = client.get("https://localhost:3060/hello")
    println(response.bodyAsText())
    client.close()
}

suspend fun main() {
    val logger = KotlinLogging.logger { }

    val (keyPairClient, certClient) = generateCert("client")
    val (keyPairServer, certServer) = generateCert("server")

    thread { server(keyPairServer, certServer, certClient) }

    Thread.sleep(1000)

    client(keyPairClient, certClient, certServer)
}

