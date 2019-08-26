import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Database {
    private val ip = "localhost:8080"

    fun createUser(name: String, publicKey: String): JsonObject? {
        val json = "{" +
                "\"name\": \"$name\", " +
                "\"publicKey\": \"$publicKey\"" +
                "}"
        return post("http://$ip/user", json)
    }

    fun sendMessage(idFrom: String, idFor: String, message: String): JsonObject? {
        val json = "{" +
                "\"idFrom\": \"$idFrom\"," +
                "\"idFor\": \"$idFor\"," +
                "\"message\": \"$message\"" +
                "}"
        return post("http://$ip/message/", json)
    }

    private fun post(url: String, json: String): JsonObject? {
        // init connection
        val connection = URL(url)
            .openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        // add json package to request
        connection.setRequestProperty("Content-Type", "application/json")

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
        val response = StringBuilder()
        try {
            val responseReader = BufferedReader(
                InputStreamReader(connection.inputStream, "utf-8")
            )
            while (true) {
                val responseLine: String? = responseReader.readLine() ?: break
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
        val query = URL("http://$ip/user/search/" +
                "findByName?name=$name")
            .readText(Charsets.UTF_8)

        // response into object
        return JsonParser()
            .parse(query)
            .asJsonObject
    }

    fun findMessageByIdFromAndIdFor(idFrom: String, idFor: String): JsonArray {
        // request and response
        val query = URL("http://$ip/message/search/" +
                "findByIdFromAndIdFor?idFrom=$idFrom&idFor=$idFor")
            .readText(Charsets.UTF_8)

        // getting array of messages from response
        val json = JsonParser()
            .parse(query)
            .asJsonObject["_embedded"] as JsonObject

        return json["message"].asJsonArray
    }

    fun getAllUsers(): JsonArray {
        val query = URL("http://$ip/user")
            .readText(Charsets.UTF_8)
        val json = JsonParser()
            .parse(query)
            .asJsonObject["_embedded"] as JsonObject

        return json["user"].asJsonArray
    }
}