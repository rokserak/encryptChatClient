import java.io.File

fun main() {
    val file = File("info/user.xml")
    if (file.length().toInt() == 0) {
        Register()
    } else {
        SelectFriend()
    }
}