package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.enum.Media

class CommonMenu(helper: SQLiteOpenHelper, resources: Resources) : MediaMenu(helper, resources) {

    override fun get(): ArrayList<String> = fetch(Media.COMMON)

    override fun save(data: ArrayList<String>) {
        update(Media.COMMON, data)
    }

    override fun getMenuList(): ArrayList<SideMenuItem> = arrayListOf()

    override fun getUrlStringFrom(title: String): String? {
        val list = getMenuList()

        return list.firstOrNull { it.title == title }?.urlString
    }
}
