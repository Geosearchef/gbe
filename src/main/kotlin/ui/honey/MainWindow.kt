package ui.honey

import APPLICATION_NAME
import APPLICATION_VERSION
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.JPanel

class MainWindow : JPanel() {

    companion object {
        const val SIZE_X = 400
        const val SIZE_Y = 500
    }

    val frame = JFrame("$APPLICATION_NAME - version $APPLICATION_VERSION").also { frame ->
        frame.size = Dimension(SIZE_X, SIZE_Y)
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null) // center on screen
        frame.layout = null

        frame.iconImage = Toolkit.getDefaultToolkit().getImage(MainWindow::class.java.getResource("/icons/icon1024.png"))
    }


    init {

        // TODO: extend JPanel, create Window, add Panel, add tabbed pane, add child panels
        frame.add(this)
    }


    init {
        frame.invalidate()
        frame.isVisible = true
    }
}