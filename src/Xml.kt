import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class Xml {
    fun readXml(filePath: String): List<String> {
        // open file for reading
        val file = File(filePath)
        val doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
        doc.documentElement.normalize()

        val keys = doc.getElementsByTagName("keys")
        val key = keys.item(0) as Element

        // get private key info
        val private = key.getElementsByTagName("private")
            .item(0)
        val user = private.attributes
            .getNamedItem("user")
            .nodeValue
        val privateKey = private.textContent

        return listOf(user, privateKey)
    }

    fun writeXml(filePath: String, name: String,
                 public: String, private: String) {
        // initialize new xml document
        val doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument()

        val keys = doc.createElement("keys")
        doc.appendChild(keys)

        // public key info
        val publicKey = doc.createElement("public")
        publicKey.setAttribute("user", name)
        publicKey.appendChild(
            doc.createTextNode(public)
        )
        keys.appendChild(publicKey)

        // private key info
        val privateKey = doc.createElement("private")
        privateKey.setAttribute("user", name)
        privateKey.appendChild(
            doc.createTextNode(private)
        )
        keys.appendChild(privateKey)

        // write doc into file
        val transformer = TransformerFactory
            .newInstance()
            .newTransformer()
        val source = DOMSource(doc)
        val file = StreamResult(
            File(filePath)
        )
        transformer.transform(source, file)
    }
}