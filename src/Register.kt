import java.awt.Dimension
import java.awt.Insets
import javax.swing.*
import kotlin.system.exitProcess

class Register {
    private var frame = JFrame()
    private var panel = JPanel()
    private var textField = JTextField()
    private var registerButton = JButton("Register")
    private var username = JLabel("Username:")
    private var checkLabel = JLabel()

    private val db = Database()
    private val crypto = Crypto()
    private val xml = Xml()

    init {
        panel.layout = com.intellij.uiDesigner.core.GridLayoutManager(4, 1, Insets(0, 0, 0, 0), -1, -1)
        panel.preferredSize = Dimension(150, 150)
        panel.add(
            textField,
            com.intellij.uiDesigner.core.GridConstraints(
                1,
                0,
                1,
                1,
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
            checkLabel,
            com.intellij.uiDesigner.core.GridConstraints(
                2,
                0,
                1,
                1,
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
        panel.add(
            username,
            com.intellij.uiDesigner.core.GridConstraints(
                0,
                0,
                1,
                1,
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
        panel.add(
            registerButton,
            com.intellij.uiDesigner.core.GridConstraints(
                3,
                0,
                1,
                1,
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

        frame.contentPane = panel
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true

        registerButton.addActionListener {
            val name = textField.text

            // check if user exist
            // if try throw error then user doesn't exist
            try {
                db.findUserByName(name)
                checkLabel.text = "Username already taken :("
            } catch (e: Exception) {
                val keys = crypto.generateKeys()
                try {
                    db.createUser(name, crypto.keyToBase64(keys.public))
                    textField.text = ""
                    checkLabel.text =""

                    // save your data in local xml file
                    xml.writeXml("info/user.xml", name,
                        crypto.keyToBase64(keys.public),
                        crypto.keyToBase64(keys.private)
                    )
                } catch (e: Exception) {
                    checkLabel.text = "failed to create user"
                }
            }
        }

    }
}