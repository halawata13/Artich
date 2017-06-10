package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.enum.Media

class CommonMenu(helper: SQLiteOpenHelper, resources: Resources) : MediaMenu(helper, resources) {

    override fun get(): ArrayList<String> {
        return fetch(Media.COMMON)
    }

    override fun add(name: String) {
        insert(Media.COMMON, name)
    }

    override fun remove(index: Int) {
        delete(Media.COMMON, index + 1)
    }

    override fun save(data: ArrayList<String>) {
        update(Media.COMMON, data)
    }

    override fun getMenuList(): ArrayList<SideMenuItem> {
        return arrayListOf()
    }

    fun reset() {
        val data = arrayListOf<String>("kotlin", "Swift", "JavaScript")
        update(Media.COMMON, data)
    }
}
