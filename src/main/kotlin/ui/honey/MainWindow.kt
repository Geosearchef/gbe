package ui.honey

import APPLICATION_NAME
import APPLICATION_VERSION
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.extras.components.FlatButton
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.*
import javax.swing.BorderFactory.createEmptyBorder as emptyBorder

class MainWindow : JPanel() {

    companion object {
        const val SIZE_X = 400
        const val SIZE_Y = 500
    }

    val settingsButton = FlatButton().apply {
        buttonType = FlatButton.ButtonType.toolBarButton
        isFocusable = false
        icon = FlatSVGIcon(MainWindow::class.java.getResource("/icons/settings-icon-light.svg"))
    }
    val menuBar = JMenuBar().apply {
        UIManager.put("TitlePane.menuBarEmbedded", true)
        add(Box.createHorizontalGlue())
        add(settingsButton)
    }

    // TODO: move the init code to the init block for readability ???
    val frame = JFrame("$APPLICATION_NAME - version $APPLICATION_VERSION").also { frame ->
        frame.size = Dimension(SIZE_X, SIZE_Y)
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null) // center on screen
        frame.layout = BorderLayout()

        frame.iconImage = Toolkit.getDefaultToolkit().getImage(MainWindow::class.java.getResource("/icons/icon1024.png"))

        frame.jMenuBar = menuBar
    }

    val sendReceivePanel = SendReceivePanel()
    val settingsPanel = SettingsPanel()

    // build window
    init {
        this.layout = BorderLayout()
        this.border = emptyBorder(10, 20, 20, 20)

        this.add(sendReceivePanel, BorderLayout.CENTER)

        frame.contentPane.add(this, BorderLayout.CENTER)
    }

    // register listeners
    init {
        settingsButton.addActionListener {
            if(settingsPanel in this.components) {
                this.remove(settingsPanel)
                this.add(sendReceivePanel) // TODO: transmission in progress?
            } else {
                this.removeAll()
                this.add(settingsPanel)
            }

            this.revalidate() // remove invalidated the containment hierachy -> revalidate
            this.repaint() // https://docs.oracle.com/javase/tutorial/uiswing/components/jcomponent.html#custompaintingapi - Always invoke repaint after revalidate
        }
    }

    // show window
    init {
        frame.invalidate()
        frame.isVisible = true
    }
}