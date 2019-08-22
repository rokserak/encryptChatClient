fun main() {
    val me = Crypto()
    var message = "hello world"
    var data = me.encrypt(message)
    println(data)
    var decrypt = me.decrypt(data)
    println(decrypt)
}