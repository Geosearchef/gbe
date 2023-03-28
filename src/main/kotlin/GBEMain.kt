import java.util.*

const val BROADCAST_PORT = 31401
const val BROADCAST_MULTICAST_ADDRESS = "239.31.40.1"  // administratively scoped multicast address, for local use only
//const val BROADCAST_MULTICAST_ADDRESS = "255.255.255.255"

const val DATA_PORT = 31402
const val APPLICATION_VERSION: Byte = 1

object GBEMain {

    val running = true
    val identifier = "none"
    val guid = UUID.randomUUID().toString()

}


fun main(args: Array<String>) {
    println("Starting GBE app")
}



