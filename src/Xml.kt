import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun readXml() {
    // open file for reading
    val file = File("keys.xml")
    val doc = DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(file)
    doc.documentElement.normalize()

    val keys = doc.getElementsByTagName("keys")
    for (i in 0 until keys.length) {
        val key = keys.item(i) as Element

        // get public key info
        val pub = key.getElementsByTagName("public")
            .item(0)
        val pubOwner = pub.attributes
            .getNamedItem("owner")
            .nodeValue
        val pubKey = pub.textContent

        println("Public key:")
        println("owner $pubOwner")
        println("key $pubKey")

        // get private key info
        val priv = key.getElementsByTagName("private")
            .item(0)
        val privOwner = priv.attributes
            .getNamedItem("owner")
            .nodeValue
        val privKey = pub.textContent

        println("Private Key")
        println("owner $privOwner")
        println("key $privKey")
    }
}

fun writeXml() {
    // initialize new xml document
    var doc = DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .newDocument()

    var keys = doc.createElement("keys")
    doc.appendChild(keys)

    // public key info
    var publicKey = doc.createElement("public")
    publicKey.setAttribute("owner", "some id")
    publicKey.appendChild(
        doc.createTextNode("some key added later")
    )
    keys.appendChild(publicKey)

    // private key info
    var privateKey = doc.createElement("private")
    privateKey.setAttribute("owner", "some id")
    privateKey.appendChild(
        doc.createTextNode("some key added later")
    )
    keys.appendChild(privateKey)

    // write doc into file
    val transformer = TransformerFactory
        .newInstance()
        .newTransformer()
    val source = DOMSource(doc)
    val file = StreamResult(
        File("keys.xml")
    )
    transformer.transform(source, file)
}

fun main() {
    writeXml()
}