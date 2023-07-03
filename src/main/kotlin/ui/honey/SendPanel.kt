package ui.honey

import transmission.protocol.BroadcastProtocolMessage
import java.awt.*
import javax.swing.*
import javax.swing.BorderFactory.createEmptyBorder as emptyBorder

class SendPanel : JPanel() {

    val receiverList = JPanel()

    init {
        this.layout = BorderLayout()
//        this.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        this.minimumSize = Dimension(0, 300)
        this.maximumSize = Dimension(Int.MAX_VALUE, 300)
        this.preferredSize = Dimension(400, 300)

        this.add(JLabel("Send").apply {
            font = font.deriveFont(Font.BOLD, 16.0f)
            border = emptyBorder(0, 0, 5, 0)
        }, BorderLayout.NORTH)

        receiverList.layout = BoxLayout(receiverList, SwingConstants.VERTICAL)
        this.add(receiverList)
    }

    fun setReceiverList(receivers: List<BroadcastProtocolMessage.PeerInfo>) {
        receiverList.removeAll()
        receivers.forEach { receiverList.add(receiverListCellElementFactory(it)) }

        this.revalidate()
        this.repaint()
    }

    fun onSendFileClicked(receiver: BroadcastProtocolMessage.PeerInfo) {
        val fileChooser = JFileChooser()
        val result = fileChooser.showOpenDialog(this)

        if(result == JFileChooser.APPROVE_OPTION) {
            val path = fileChooser.selectedFile.toPath() // TODO: multiple files
            println("Selected file ${path.toAbsolutePath()} to send to ${receiver.identifier}")
        }
    }

    fun onSendTextClicked(receiver: BroadcastProtocolMessage.PeerInfo) {
        val text = JOptionPane.showInputDialog(this, "Text to send:", "Send Text", JOptionPane.QUESTION_MESSAGE)
        println("Entered text \"$text\" to send to ${receiver.identifier}")
    }

    fun receiverListCellElementFactory(receiver: BroadcastProtocolMessage.PeerInfo) : JPanel {
        val backgroundColor = Color.WHITE

        val infoPanel = JPanel()
        infoPanel.background = backgroundColor

        infoPanel.layout = GridLayout(2, 2, 0, 0)

        infoPanel.add(JLabel(receiver.identifier).apply { font = font.deriveFont(Font.BOLD, 14.0f) })
        infoPanel.add(JLabel("ver: ${receiver.version}").apply {
            font = font.deriveFont(10.0f)
            horizontalAlignment = SwingConstants.RIGHT
        })
        infoPanel.add(JLabel(receiver.system.replace("_", " - ")).apply { font = font.deriveFont(10.0f) })

        val cellPanel = JPanel()
        cellPanel.layout = BorderLayout()
        cellPanel.background = backgroundColor
        cellPanel.border = emptyBorder(10, 10, 10, 10)
        cellPanel.maximumSize = Dimension(Int.MAX_VALUE, 80)

        cellPanel.add(infoPanel, BorderLayout.WEST)

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, SwingConstants.VERTICAL)
        buttonPanel.background = backgroundColor

        val sendFileButton = JButton("Send File").apply {
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }
        Dimension(100, 30).let {
            sendFileButton.maximumSize = it
            sendFileButton.minimumSize = it
        }
        sendFileButton.setMinMaxSize(Dimension(100, 35))
        sendFileButton.alignmentY = Component.CENTER_ALIGNMENT
        sendFileButton.addActionListener { onSendFileClicked(receiver) }
        buttonPanel.add(sendFileButton)

        val sendTextButton = JButton("Send Text").apply {
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }
        sendTextButton.setMinMaxSize(Dimension(100, 25))
        sendTextButton.alignmentY = Component.CENTER_ALIGNMENT
        sendTextButton.addActionListener { onSendTextClicked(receiver) }
        buttonPanel.add(sendTextButton)

        cellPanel.add(buttonPanel, BorderLayout.EAST)

        return cellPanel
    }

    fun JComponent.setMinMaxSize(dim: Dimension) {
        this.minimumSize = dim
        this.maximumSize = dim
    }

}