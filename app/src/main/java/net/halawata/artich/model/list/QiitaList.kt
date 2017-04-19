package net.halawata.artich.model.list

import android.util.Log
import net.halawata.artich.entity.QiitaArticle
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class QiitaList: MediaList {

    override fun parse(content: String): ArrayList<QiitaArticle>? {
        try {
            val items = JSONArray(content)
            val articles = ArrayList<QiitaArticle>()

            for (i in 0..(items).length() - 1) {
                val row = items.getJSONObject(i)

                val article = QiitaArticle(
                        id = i.toLong(),
                        title = row.get("title") as? String ?: "",
                        url = row.get("url") as? String ?: "",
                        pubDate = row.get("updated_at") as? String ?: ""
                )

                articles.add(article)
            }

            return articles
        } catch (ex: JSONException) {
            Log.e("JSONException", ex.message)
            return null
        }
    }

}
