package ui.honey

import transmission.protocol.BroadcastProtocolMessage
import java.awt.*
import javax.swing.*

class SendPanel : JPanel() {

    val receiverList = JList<BroadcastProtocolMessage.PeerInfo>()

    init {
        this.layout = BorderLayout()
        this.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        receiverList.cellRenderer = ReceiverListCellRenderer
        receiverList.visibleRowCount = 6
        this.add(receiverList, BorderLayout.CENTER)
    }

    fun setReceiverList(receivers: List<BroadcastProtocolMessage.PeerInfo>) {
        receiverList.setListData(receivers.toTypedArray())
    }


    object ReceiverListCellRenderer : ListCellRenderer<BroadcastProtocolMessage.PeerInfo> {
        override fun getListCellRendererComponent(
            list: JList<out BroadcastProtocolMessage.PeerInfo>?,
            value: BroadcastProtocolMessage.PeerInfo?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            var backgroundColor = list?.background ?: Color.WHITE
            if(isSelected) {
                backgroundColor = Color(220, 220, 220)
            }

            val infoPanel = JPanel()
            infoPanel.background = backgroundColor

            infoPanel.layout = GridLayout(2, 2, 0, 0)

            infoPanel.add(JLabel(value?.identifier ?: "???").apply { font = font.deriveFont(Font.BOLD, 14.0f) })
            infoPanel.add(JLabel("ver: ${value?.version ?: "???"}").apply {
                font = font.deriveFont(10.0f)
                horizontalAlignment = SwingConstants.RIGHT
            })
            infoPanel.add(JLabel(value?.system?.replace("_", " - ") ?: "???").apply { font = font.deriveFont(10.0f) })

            val cellPanel = JPanel()
            cellPanel.layout = BorderLayout()
            cellPanel.background = backgroundColor
            cellPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            cellPanel.add(infoPanel, BorderLayout.WEST)

            val sendButton = JButton("Send").apply {
                horizontalAlignment = SwingConstants.CENTER
                verticalAlignment = SwingConstants.CENTER
            }
            sendButton.addActionListener { println("I was clicked") }
            cellPanel.add(sendButton, BorderLayout.EAST)


            return cellPanel
        }

    }
}