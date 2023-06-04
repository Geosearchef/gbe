package ui.honey

import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.BorderFactory.createEmptyBorder as emptyBorder

class ReceivePanel : JPanel() {

    init {
        this.layout = BorderLayout()
//        this.minimumSize = Dimension(200, 200)
//        this.maximumSize = Dimension(1000, 1000)
//        this.pref

//        this.border = BorderFactory.createCompoundBorder(
//            BorderFactory.createLineBorder(Color.red),
//            this.border
//        )

        this.add(JLabel("Receive").apply {
            font = font.deriveFont(Font.BOLD, 16.0f)
            border = emptyBorder(0, 0, 5, 0)
        }, BorderLayout.NORTH)

        this.add(JLabel("Ready for incoming transfers", SwingConstants.CENTER), BorderLayout.CENTER)
    }
}