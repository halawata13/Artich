package net.halawata.artich.model.config

import net.halawata.artich.entity.QiitaTag
import org.json.JSONArray
import org.json.JSONException

class QiitaTagList {

    fun parse(content: String): ArrayList<QiitaTag>? {
        try {
            val list = ArrayList<QiitaTag>()

            val items = JSONArray(content)

            for (i in 0..(items).length() - 1) {
                val row = items.getJSONObject(i)
                list.add(QiitaTag(
                        id = i.toLong(),
                        title = row.getString("id") ?: "",
                        selected = false
                ))
            }

            return list

        } catch (ex: JSONException) {
            ex.printStackTrace()
            return null
        }
    }
}
