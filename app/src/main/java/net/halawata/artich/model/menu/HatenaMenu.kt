package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.R
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.enum.Media
import net.halawata.artich.model.ApiUrlString

class HatenaMenu(helper: SQLiteOpenHelper, resources: Resources) : MediaMenu(helper, resources) {

    override fun get(): ArrayList<String> {
        return fetch(Media.HATENA)
    }

    override fun add(name: String) {
        insert(Media.HATENA, name)
    }

    override fun remove(index: Int) {
        delete(Media.HATENA, index + 1)
    }

    override fun save(data: ArrayList<String>) {
        update(Media.HATENA, data)
    }

    override fun getMenuList(): ArrayList<SideMenuItem> {
        val menuItems: ArrayList<SideMenuItem> = arrayListOf()

        var id: Long = 0

        val menuList = fetch(Media.COMMON) + fetch(Media.HATENA)

        menuItems.add(SideMenuItem(
                id = id++,
                urlString = ApiUrlString.Hatena.newEntry,
                title = resources.getString(R.string.new_entry)
        ))

        menuItems.add(SideMenuItem(
                id = id++,
                urlString = ApiUrlString.Hatena.hotEntry,
                title = resources.getString(R.string.hot_entry)
        ))

        menuList.forEach { title ->
            menuItems.add(SideMenuItem(
                    id = id++,
                    urlString = ApiUrlString.Hatena.get(title),
                    title = title
            ))
        }

        return menuItems
    }
}
