package net.halawata.artich.model.menu

import android.content.res.Resources
import net.halawata.artich.R
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.model.ApiUrlString

class QiitaMenu(val resources: Resources) {

    fun getList(): ArrayList<SideMenuItem> {
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
