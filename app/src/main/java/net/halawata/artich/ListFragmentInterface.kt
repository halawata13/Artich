package net.halawata.artich

import net.halawata.artich.model.list.MediaList

interface ListFragmentInterface {

    val list: MediaList

    fun request(urlString: String)

    fun success(content: String)

    fun fail()

    fun applyFilter()

    fun reload()
}
