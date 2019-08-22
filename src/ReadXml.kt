import java.io.ByteArrayInputStream
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    var factory = DocumentBuilderFactory.newInstance()
    var builder = factory.newDocumentBuilder()
    var xmlStrBuilder = StringBuilder()
    xmlStrBuilder.append("<?xml version=\"1.0\"?> <class> </class>")
    var input = ByteArrayInputStream(xmlStrBuilder.toString().toByteArray())
    var doc = builder.parse(input)

    var root = doc.documentElement
    print(root.getAttribute("class"))
}