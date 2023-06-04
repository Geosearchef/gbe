import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
//    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("com.formdev:flatlaf:3.1.1")
    implementation("com.formdev:flatlaf-extras:3.1.1")
    implementation("com.formdev:svgSalamander:1.1.4")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.github.Geosearchef:spark:master")
    implementation("com.sparkjava:spark-core:2.9.4")
//    testImplementation(kotlin("test"))
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client
    // https://mvnrepository.com/artifact/io.ktor/ktor-client
    implementation("io.ktor:ktor-client:2.3.1")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.1")


}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

application {
    mainClass.set("GBEMainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClass
            )
        )
    }
}