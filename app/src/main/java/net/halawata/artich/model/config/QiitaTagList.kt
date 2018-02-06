package net.halawata.artich.model.config

import net.halawata.artich.entity.QiitaTag
import net.halawata.artich.model.Log
import org.json.JSONArray
import org.json.JSONException

class QiitaTagList {

    fun parse(content: String, selectedList: ArrayList<String>): ArrayList<QiitaTag>? {
        try {
            val list = ArrayList<QiitaTag>()

            val items = JSONArray(content)

            for (i in 0 until (items).length()) {
                val row = items.getJSONObject(i)
                val title = row.getString("id") ?: break
                val selected = selectedList.contains(title)

                list.add(QiitaTag(
                        id = i.toLong(),
                        title = title,
                        selected = selected
                ))
            }

            list.sortBy { item -> item.title }

            return list

        } catch (ex: JSONException) {
            Log.e(ex.message)
            return null
        }
    }
}
