package net.halawata.artich.model.menu

import net.halawata.artich.entity.SideMenuItem

interface MediaMenuInterface {

    fun get(): ArrayList<String>

    fun add(name: String)

    fun remove(index: Int)

    fun save(data: ArrayList<String>)

    fun getMenuList(): ArrayList<SideMenuItem>
}
