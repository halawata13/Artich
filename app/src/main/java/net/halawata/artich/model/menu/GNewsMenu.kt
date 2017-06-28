package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.R
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.enum.Media
import net.halawata.artich.model.ApiUrlString

class GNewsMenu(helper: SQLiteOpenHelper, resources: Resources) : MediaMenu(helper, resources) {

    override fun get(): ArrayList<String> {
        return fetch(Media.GNEWS)
    }

    override fun add(name: String) {
        insert(Media.GNEWS, name)
    }

    override fun remove(index: Int) {
        delete(Media.GNEWS, index + 1)
    }

    override fun save(data: ArrayList<String>) {
        update(Media.GNEWS, data)
    }

    override fun getMenuList(): ArrayList<SideMenuItem> {
        val menuItems: ArrayList<SideMenuItem> = arrayListOf()

        var id: Long = 0
        val menuList = resources.getStringArray(R.array.menu_list)

        menuItems.add(SideMenuItem(
                id = id++,
                urlString = ApiUrlString.Qiita.newEntry,
                title = resources.getString(R.string.new_entry)
        ))

        menuList.forEach { title ->
            menuItems.add(SideMenuItem(
                    id = id++,
                    urlString = ApiUrlString.Qiita.get(title),
                    title = title
            ))
        }

        return menuItems
    }
}
