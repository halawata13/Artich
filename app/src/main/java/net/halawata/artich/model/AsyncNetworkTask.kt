package net.halawata.artich.model

import android.net.Uri
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class AsyncNetworkTask : AsyncTask<String, Int, String>() {

    private var method = Method.GET
    private var useCache = true
    private var responseCode: Int? = null

    var onResponse: ((responseCode: Int?, content: String?) -> Unit)? = null

    override fun doInBackground(vararg params: String?): String? {
        var content: String? = ""
        var connection: HttpURLConnection? = null

        try {
            val url = URL(params.first())
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method.toString()

            if (!useCache) {
                connection.addRequestProperty("Cache-Control", "no-cache")
            }

            // POST の場合のパラメータ
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
            // 400, 500 系はこの例外で飛んでくる
            val reader = BufferedReader(InputStreamReader(connection?.errorStream, "UTF-8"))

            var line = reader.readLine()
            while (line != null) {
                content += line
                line = reader.readLine()
            }

            reader.close()
            Log.e(ex.message)

        } catch (ex: IOException) {
            content = null
            Log.e(ex.message)

        } finally {
            connection?.disconnect()
        }

        return content
    }

    override fun onPostExecute(result: String?) {
        // 通信完了時のコールバック
        onResponse?.invoke(responseCode, result)
    }

    /**
     * リクエストする
     */
    fun request(urlString: String, method: Method, params: Map<String, String>? = null, useCache: Boolean = true) {
        this.method = method
        this.useCache = useCache

        val builder = Uri.Builder()

        params?.forEach { (k, v) ->
            builder.appendQueryParameter(k, v)
        }

        var paramsString = builder.build().toString()

        when (method) {
            Method.GET -> {
                // GET はパラメータを URL に付加する
                execute(urlString + paramsString)
            }
            Method.POST -> {
                // POST はパラメータを引数で渡す
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
