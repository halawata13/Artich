package net.halawata.artich.model.mute

import net.halawata.artich.entity.ListItem

interface MediaMuteInterface {

    fun get(): ArrayList<ListItem>

    fun add(name: String)

    fun remove(id: Int)

    fun save(data: ArrayList<String>)
}
