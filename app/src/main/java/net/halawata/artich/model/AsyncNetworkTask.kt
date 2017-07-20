package net.halawata.artich.model

import android.net.Uri
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class AsyncNetworkTask : AsyncTask<String, Int, String>() {

    var method = Method.GET
    var responseCode: Int? = null
    var onResponse: ((responseCode: Int?, content: String?) -> Unit)? = null

    override fun doInBackground(vararg params: String?): String? {
        var content: String? = ""
        var connection: HttpURLConnection? = null

        try {
            val url = URL(params.first())
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method.toString()
            connection.useCaches = false

            if (method == Method.POST) {
                val printWriter = PrintWriter(connection.outputStream)
                printWriter.print(params[1])
                printWriter.close()
            }

            responseCode = connection.responseCode

            val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))

            var line = reader.readLine()
            while (line != null) {
                content += line
                line = reader.readLine()
            }

            reader.close()

        } catch (ex: FileNotFoundException) {
            val reader = BufferedReader(InputStreamReader(connection?.errorStream, "UTF-8"))

            var line = reader.readLine()
            while (line != null) {
                content += line
                line = reader.readLine()
            }

            reader.close()

        } catch (ex: IOException) {
            ex.printStackTrace()

            content = null

        } finally {
            connection?.disconnect()
        }

        return content
    }

    override fun onPostExecute(result: String?) {
        onResponse?.invoke(responseCode, result)
    }

    fun request(urlString: String, method: Method, params: Map<String, String>? = null) {
        this.method = method

        val builder = Uri.Builder()

        params?.forEach { (k, v) ->
            builder.appendQueryParameter(k, v)
        }
        var paramsString = builder.build().toString()

        when (method) {
            Method.GET -> {
                execute(urlString + paramsString)
            }
            Method.POST -> {
                paramsString = paramsString.drop(1)
                execute(urlString, paramsString)
            }
        }
    }

    enum class Method {
        GET,
        POST,
    }
}
