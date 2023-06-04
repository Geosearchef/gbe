package ui

import com.formdev.flatlaf.FlatLightLaf
import transmission.protocol.BroadcastProtocolMessage
import ui.honey.MainWindow

object HoneyController: UIController {

    var mainWindow: MainWindow? = null

    override fun init() {
        FlatLightLaf.setup()

        mainWindow = MainWindow()
    }

    override fun updateReceivers(receivers: List<BroadcastProtocolMessage.PeerInfo>) {
        mainWindow?.sendReceivePanel?.sendPanel?.setReceiverList(receivers)
    }

}