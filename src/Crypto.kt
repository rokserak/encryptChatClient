import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

class Crypto {
    private val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
    private val kf = KeyFactory.getInstance("RSA")

    fun encrypt(text: String, publicKey: PublicKey): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val input = text.toByteArray(Charsets.UTF_8)
        cipher.update(input)
        return cipher.doFinal()
    }

    fun decrypt(data: ByteArray, privateKey: PrivateKey): String {
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        cipher.update(data)
        return cipher.doFinal().toString(Charsets.UTF_8)
    }

    // get public and private key
    fun generateKeys(): KeyPair {
        val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(4096)
        return generator.generateKeyPair()
    }

    fun keyToBase64(key: Key): String {
        return Base64.getEncoder().encodeToString(key.encoded)
    }

    fun base64ToPrivateKey(key: String): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(
            Base64.getDecoder().decode(key)
        )
        return kf.generatePrivate(keySpec)
    }

    fun base64ToPublicKey(key: String): PublicKey {
        val keySpec = X509EncodedKeySpec(
            Base64.getDecoder().decode(key)
        )
        return kf.generatePublic(keySpec)
    }
}