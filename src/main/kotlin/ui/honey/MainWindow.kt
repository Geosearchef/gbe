package ui.honey

import APPLICATION_NAME
import APPLICATION_VERSION
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.*

class MainWindow : JPanel() {

    companion object {
        const val SIZE_X = 400
        const val SIZE_Y = 500
    }

    // TODO: move the init code to the init block for readability ???
    val frame = JFrame("$APPLICATION_NAME - version $APPLICATION_VERSION").also { frame ->
        frame.size = Dimension(SIZE_X, SIZE_Y)
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null) // center on screen
        frame.layout = BorderLayout()

        frame.iconImage = Toolkit.getDefaultToolkit().getImage(MainWindow::class.java.getResource("/icons/icon1024.png"))
    }


    val tabbedPane = JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT).apply {
        // these properties are part of the look and feel and therefore not statically typed, this could be moved to a properties file
        putClientProperty("JTabbedPane.showTabSeparators", true)
        putClientProperty("JTabbedPane.showContentSeparator", true)
        putClientProperty("JTabbedPane.tabAreaAlignment", "fill")
        putClientProperty("JTabbedPane.tabAlignment", SwingConstants.CENTER)
        putClientProperty("JTabbedPane.tabWidthMode", "equal")
        putClientProperty("JTabbedPane.tabIconPlacement", SwingConstants.TOP)
    }

    val sendPanel = SendPanel()
    val receivePanel = ReceivePanel()
    val settingsPanel = SettingsPanel()


    init {
        this.layout = BorderLayout()

        tabbedPane.addTab("Send", UIManager.getIcon("FileChooser.upFolderIcon"), sendPanel)
        tabbedPane.addTab("Receive", UIManager.getIcon("FileChooser.listViewIcon"), receivePanel)
        tabbedPane.addTab("Settings", UIManager.getIcon("FileChooser.detailsViewIcon"), settingsPanel)

        this.add(tabbedPane, BorderLayout.CENTER)
        frame.contentPane.add(this, BorderLayout.CENTER) // should this be frame.getContentPane().add() ?
    }


    init {
        frame.invalidate()
        frame.isVisible = true
    }
}