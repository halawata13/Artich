package net.halawata.artich.model.list

import android.content.Context
import net.halawata.artich.entity.QiitaArticle
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.Log
import net.halawata.artich.model.mute.QiitaMute
import org.json.JSONArray
import org.json.JSONException
import kotlin.collections.ArrayList

class QiitaList(): MediaList {

    override val dateFormat = "yyyy-MM-dd'T'HH:mm:ssz"

    override fun parse(content: String): ArrayList<QiitaArticle>? {
        try {
            val items = JSONArray(content)
            val articles = ArrayList<QiitaArticle>()

            for (i in 0 until (items).length()) {
                val row = items.getJSONObject(i)

                val article = QiitaArticle(
                        id = i.toLong(),
                        title = row.getString("title") ?: "",
                        url = row.getString("url") ?: "",
                        pubDate = formatDate(row.getString("updated_at")) ?: "",
                        user = row.getJSONObject("user").getString("id") ?: ""
                )

                articles.add(article)
            }

            articles.sortByDescending { article -> article.pubDate }

            return articles

        } catch (ex: JSONException) {
            Log.e(ex.message)
            return null
        }
    }

    fun filter(data: ArrayList<QiitaArticle>, context: Context): ArrayList<QiitaArticle> {
        val filtered = ArrayList<QiitaArticle>()
        val helper = DatabaseHelper(context)
        val mute = QiitaMute(helper)
        val muteList = mute.getMuteList()

        data.forEach { item ->
            if (!muteList.contains(item.user)) {
                filtered.add(item)
            }
        }

        return filtered
    }
}
