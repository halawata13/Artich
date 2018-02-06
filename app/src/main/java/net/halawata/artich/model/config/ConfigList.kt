package net.halawata.artich.model.config

import android.content.res.Resources
import net.halawata.artich.R
import net.halawata.artich.entity.ConfigListItem
import net.halawata.artich.enum.Media

class ConfigList(val resources: Resources, val type: Type) {

    fun getMenuList(): ArrayList<ConfigListItem> {
        val menuItems: ArrayList<ConfigListItem> = arrayListOf()

        var id: Long = 0

        // 共通があるのはメニュー編集のみ
        if (type == Type.MENU) {
            menuItems.add(ConfigListItem(
                    id = id++,
                    mediaId = Media.COMMON,
                    title = resources.getString(R.string.common_list_name)
            ))
        }

        menuItems.add(ConfigListItem(
                id = id++,
                mediaId = Media.GNEWS,
                title = resources.getString(R.string.gnews_list_name)
        ))

        menuItems.add(ConfigListItem(
                id = id++,
                mediaId = Media.QIITA,
                title = resources.getString(R.string.qiita_list_name)
        ))

        menuItems.add(ConfigListItem(
                id = id,
                mediaId = Media.HATENA,
                title = resources.getString(R.string.hatena_list_name)
        ))

        return menuItems
    }

    fun getMediaId(title: String): Media? =
            when (title) {
                resources.getString(R.string.common_list_name) -> Media.COMMON
                resources.getString(R.string.hatena_list_name) -> Media.HATENA
                resources.getString(R.string.qiita_list_name) -> Media.QIITA
                resources.getString(R.string.gnews_list_name) -> Media.GNEWS
                else -> null
            }

    enum class Type(val num: Int) {
        MENU(0),
        MUTE(1),
    }
}
