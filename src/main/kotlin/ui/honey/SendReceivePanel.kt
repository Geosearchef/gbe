package ui.honey

import javax.swing.*

class SendReceivePanel : JPanel() {

    val sendPanel = SendPanel()
    val receivePanel = ReceivePanel()

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)

        this.add(sendPanel)
        this.add(Box.createVerticalStrut(15))
        this.add(JSeparator(SwingConstants.HORIZONTAL))
        this.add(Box.createVerticalStrut(15))
        this.add(receivePanel)
    }

}