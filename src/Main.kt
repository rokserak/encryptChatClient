import java.io.File

fun main() {
    val file = File("info/user.xml")
    if (!file.exists()) file.createNewFile()

    if (file.length().toInt() == 0) {
        Register()
    } else {
        SelectFriend()
    }
}