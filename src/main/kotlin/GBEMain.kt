import mu.KotlinLogging
import ui.HoneyController
import ui.UIController
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

//    val frame = JFrame("GBE").also { frame ->
//        frame.size = Dimension(400,500)
//        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//        frame.setLocationRelativeTo(null)
//        frame.layout = null
//
//        val textField = JTextField("Hello World").apply {
//            bounds = Rectangle(20, 20, 100, 30)
//            frame.add(this)
//        }
//
//        JButton("Press me").apply {
//            bounds = Rectangle(20, 55, 100, 30)
//            addActionListener {
//                println(textField.text)
//            }
//            frame.add(this)
//        }
//
//        frame.invalidate()
//        frame.isVisible = true
//    }
}



