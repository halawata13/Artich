package net.halawata.artich.model.list

import android.util.Log
import net.halawata.artich.entity.HatenaArticle
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class HatenaList: MediaList {

    override fun parse(content: String): ArrayList<HatenaArticle>? {
        try {
            val json = JSONObject(content)
            val items = json.getJSONArray("items")
            val articles = ArrayList<HatenaArticle>()

            for (i in 0..(items.length() - 1)) {
                val row = items.getJSONObject(i)

                val article = HatenaArticle(
                        id = i.toLong(),
                        title = row.get("title") as? String ?: "",
                        url = row.get("link") as? String ?: "",
                        pubDate = row.get("pubDate") as? String ?: ""
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
