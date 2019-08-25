import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Http {
    fun createUser(name: String, publicKey: String): JsonObject? {
        // init connection
        var connection = URL("http://localhost:8080/user")
            .openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        // add json package to request
        connection.setRequestProperty("Content-Type", "application/json")
        var json = "{" +
                "\"name\": \"$name\", " +
                "\"publicKey\": \"$publicKey\"" +
                "}"

        // send request
        try {
            val outputStream = connection.outputStream
            val jsonBytes = json.toByteArray(Charsets.UTF_8)
            outputStream.write(jsonBytes)
        } catch (e: Exception) {
            print("failed to send json")
            return null
        }

        // read response
        var response = StringBuilder()
        try {
            val responseReader = BufferedReader(
                InputStreamReader(
                    connection.inputStream,
                    "utf-8"
                )
            )
            while (true) {
                var responseLine: String? = responseReader.readLine() ?: break
                response.append(responseLine)
            }
        } catch (e: Exception) {
            println("failed to read response")
            return null
        }

        // make object from response
        return JsonParser()
            .parse(response.toString())
            .asJsonObject
    }

    fun findUserByName(name: String): JsonObject {
        //request and response
        val query = URL("http://localhost:8080/user/search/findByName?name=$name")
            .readText(Charsets.UTF_8)

        // response into object
        return JsonParser()
            .parse(query)
            .asJsonObject
    }
}

fun main() {
    var a = Http()
    a.findUserByName("nina")
}
