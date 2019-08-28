package chatClient

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.ColorModel
import javax.swing.*

class SelectFriend {
    private var frame = JFrame("Friends")
    private var panel = JPanel()
    private val label = JLabel("Select your friend")
    private var list = JList<String>()
    private var scrollPane = JScrollPane(list)

    init {
        // GUI designer
        panel.layout = com.intellij.uiDesigner.core.GridLayoutManager(
            2, 1,
            Insets(0, 0, 0, 0),
            -1, -1
        )
        panel.preferredSize = Dimension(250, 250)
        panel.add(
            scrollPane,
            com.intellij.uiDesigner.core.GridConstraints(
                1, 0, 1, 1,
                com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
                null,
                Dimension(150, 50),
                null,
                0,
                false
            )
        )
        panel.add(
            label,
            com.intellij.uiDesigner.core.GridConstraints(
                0, 0, 1, 1,
                com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
                com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )

        fillList()

        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent?) {
                val list = evt!!.source as JList<*>
                if (evt.clickCount == 2) {
                    Chat(list.selectedValue as String)
                    //frame.isVisible = false
                }
            }
        })


        // my code
        frame.minimumSize = Dimension(250,300)
        frame.add(panel)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true
    }

    private fun fillList() {
        val jsonFriends = Database().getAllUsers()
        val friends = DefaultListModel<String>()
        for (friend in jsonFriends) {
            val name = friend.asJsonObject["name"].asString
            friends.addElement(name)
        }
        list.model = friends
    }
}