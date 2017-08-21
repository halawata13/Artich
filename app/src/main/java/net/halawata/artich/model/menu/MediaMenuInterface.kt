package net.halawata.artich.model.menu

import net.halawata.artich.entity.SideMenuItem

interface MediaMenuInterface {

    fun get(): ArrayList<String>

    fun save(data: ArrayList<String>)

    fun getMenuList(): ArrayList<SideMenuItem>

    fun getUrlStringFrom(title: String): String?
}
