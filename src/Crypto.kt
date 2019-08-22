import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher

class Crypto() {
    private val keys: KeyPair
    private val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")

    //generate public and private key
    init {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(4096)
        keys = kpg.generateKeyPair()
    }

    fun encrypt(text: String): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, keys.public)
        var input = text.toByteArray(Charsets.UTF_8)
        cipher.update(input)
        return cipher.doFinal()
    }

    fun decrypt(data: ByteArray): String {
        cipher.init(Cipher.DECRYPT_MODE, keys.private)
        cipher.update(data)
        return cipher.doFinal().toString(Charsets.UTF_8)
    }
}