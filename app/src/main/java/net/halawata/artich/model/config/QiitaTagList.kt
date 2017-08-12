package net.halawata.artich.model.config

import net.halawata.artich.entity.QiitaTag
import org.json.JSONArray
import org.json.JSONException

class QiitaTagList {

    fun parse(content: String, selectedList: ArrayList<String>): ArrayList<QiitaTag>? {
        try {
            val list = ArrayList<QiitaTag>()

            val items = JSONArray(content)

            for (i in 0..(items).length() - 1) {
                val row = items.getJSONObject(i)
                val title = row.getString("id") ?: break
                val selected = selectedList.contains(title)

                list.add(QiitaTag(
                        id = i.toLong(),
                        title = title,
                        selected = selected
                ))
            }

            return list

        } catch (ex: JSONException) {
            ex.printStackTrace()
            return null
        }
    }
}
