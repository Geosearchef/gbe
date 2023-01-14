package transmission.receiver

import APPLICATION_VERSION
import BROADCAST_PORT
import GBEMain
import transmission.protocol.BroadcastProtocolMessage
import transmission.protocol.PeerInfo
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer

object BroadcastListener {

    private const val CMD_PROBE: Byte = 30
    private const val CMD_ESTABLISH_CONNECTION: Byte = 31

    private const val CMD_PROBE_REPLY: Byte = 40

    private val datagramSocket: DatagramSocket

    init {
        datagramSocket = DatagramSocket(BROADCAST_PORT)

        Thread(::broadcastThread).start()
    }


    private fun broadcastThread() {
        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        while(GBEMain.running) {
            val requestPacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)
            datagramSocket.receive(requestPacket)

            if(requestPacket.length < 2) {
                // invalid
                continue
            }

            // parse the received packet

            val senderAddress = requestPacket.address.hostAddress
            val senderPort = requestPacket.port

            var buffer = ByteBuffer.wrap(requestPacket.data)

            val version = buffer.get()
            val cmd = buffer.get()
            val peerInfo = BroadcastProtocolMessage.getMessageFromBuffer(PeerInfo::class.java, buffer)


            // create a response
            buffer = ByteBuffer.wrap(responseBuffer)
            buffer.put(APPLICATION_VERSION)

            when(cmd) {
                CMD_PROBE -> {
                    println("Received broadcast probe from $senderAddress:$senderPort, version $version, identifier: ${peerInfo.identifier}, system: ${peerInfo.system}")

                    buffer.put(CMD_PROBE_REPLY)
                    PeerInfo(APPLICATION_VERSION, GBEMain.identifier).applyToBuffer(buffer)
                }
                CMD_ESTABLISH_CONNECTION -> {
                    throw NotImplementedError() // TODO
                }
                else -> {
                    continue
                }
            }

            val responsePacket = DatagramPacket(buffer.array(), 0, buffer.position(), InetAddress.getByName(senderAddress), senderPort)
            datagramSocket.send(responsePacket)
        }
    }

}

