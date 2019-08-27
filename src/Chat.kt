import java.awt.Dimension
import java.awt.Insets
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableModel

class Chat(friend: String) {
    private var frame = JFrame("Chat with $friend")
    private var panel = JPanel()
    private var table = JTable()
    private var textField = JTextField()
    private var sendButton = JButton("send")

    private var db = Database()
    private var crypto = Crypto()

    // your info
    private val userData = Xml().readXml("info/user.xml")
    private val user = userData[0]
    private val userPrivateKey = crypto.base64ToPrivateKey(userData[1])
    // friends info
    private val friend = friend
    private val friendPublicKey = crypto.base64ToPublicKey(
        db.findUserByName(friend)["publicKey"].asString
    )

    init {
        // all generated by Intellij GUI designer
        panel.layout = com.intellij.uiDesigner.core.GridLayoutManager(
            2, 2,
            Insets(0, 0, 0, 0),
            -1, -1
        )
        panel.preferredSize = Dimension(250, 400)
        panel.add(
            table,
            com.intellij.uiDesigner.core.GridConstraints(
                0, 0, 1, 2,
                com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
                null,
                Dimension(150, 50),
                null,
                0,
                false
            )
        )
        panel.add(
            textField,
            com.intellij.uiDesigner.core.GridConstraints(
                1, 0, 1, 1,
                com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
                com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                null,
                Dimension(150, -1),
                null,
                0,
                false
            )
        )
        panel.add(
            sendButton,
            com.intellij.uiDesigner.core.GridConstraints(
                1, 1, 1, 1,
                com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK or com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )

        // my code
        frame.minimumSize = Dimension(250,300)
        frame.add(panel)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true

        fillTable()

        // action listeners
        sendButton.addActionListener {
            val message = textField.text
            if (message == "") return@addActionListener

            try {
                println(crypto.encrypt(message, friendPublicKey))
                db.sendMessage(user, friend,
                    crypto.encrypt(message, friendPublicKey)
                )
            } catch (e: Exception) {}

            textField.text = ""
            fillTable()
        }
    }

    private fun fillTable() {
        val list = Vector<Vector<String>>()
        val messages = db.findMessageByIdFromAndIdFor(user, friend)
        for (m in messages) {
            val line = Vector<String>()
            val encrypted = m.asJsonObject["message"].toString()
            line.add(crypto.decrypt(
                encrypted.subSequence(1, encrypted.length - 1).toString()
                , userPrivateKey)
            )
            list.add(line)
        }

        val head = Vector<String>()
        head.add("ola")

        table.model = DefaultTableModel(list, head)
    }

}