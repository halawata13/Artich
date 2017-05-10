package net.halawata.artich.model.menu

import android.content.res.Resources
import net.halawata.artich.R
import net.halawata.artich.entity.Menu
import net.halawata.artich.model.ApiUrlString

class GNewsMenu(val resources: Resources) {

    fun getList(): ArrayList<Menu> {
        val menus: ArrayList<Menu> = arrayListOf()

        var id: Long = 0
        val menuList = resources.getStringArray(R.array.menu_list)

        menus.add(Menu(
                id = id++,
                urlString = ApiUrlString.Qiita.newEntry,
                title = resources.getString(R.string.new_entry)
        ))

        menuList.forEach { title ->
            menus.add(Menu(
                    id = id++,
                    urlString = ApiUrlString.Qiita.get(title),
                    title = title
            ))
        }

        return menus
    }

}
