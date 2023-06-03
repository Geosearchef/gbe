import mu.KotlinLogging
import transmission.protocol.BroadcastProtocolMessage
import ui.HoneyController
import ui.UIController
import util.getSystemIdentifier
import java.util.*

const val APPLICATION_NAME = "BearDrop"

const val BROADCAST_PORT = 31401
const val BROADCAST_MULTICAST_ADDRESS = "239.31.40.1"  // administratively scoped multicast address, for local use only
//const val BROADCAST_MULTICAST_ADDRESS = "255.255.255.255"

const val DATA_PORT = 31402
const val APPLICATION_VERSION: Byte = 1

object GBEMain {

    val logger = KotlinLogging.logger {  }

    val running = true
    val identifier = "none"
    val guid = UUID.randomUUID().toString()

    val uiController: UIController = HoneyController

}


fun main() {
    GBEMain.logger.info { "Starting GBE app" }

    GBEMain.uiController.init()

    Thread.sleep(1000)

    GBEMain.uiController.updateReceivers(listOf(
        BroadcastProtocolMessage.PeerInfo(1, "thisIsTheGuid", "kirella-6a", getSystemIdentifier()),
        BroadcastProtocolMessage.PeerInfo(1, "otherGuid", "kirella-desktop", getSystemIdentifier()),
    ))
}
