package ui.honey

import java.awt.Color
import javax.swing.JLabel
import javax.swing.JPanel

class SettingsPanel : JPanel() {

    init {
        this.add(JLabel("Settings panel"))
        background = Color.GREEN
    }

}