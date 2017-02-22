package net.halawata.artich.model

import android.os.AsyncTask
import net.halawata.artich.ListActivityInterface
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AsyncNetworkTask(val activity: ListActivityInterface): AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String {
        var content = ""

        try {
            val url = URL(params[0])
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))

            var line = reader.readLine()
            while (line != null) {
                content += line
                line = reader.readLine()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return content
    }

    override fun onPostExecute(result: String?) {
        if (result == null) {
            return
        }

        activity.updateList(result)
    }

}
