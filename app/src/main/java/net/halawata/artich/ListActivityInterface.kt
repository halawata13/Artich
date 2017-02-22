package net.halawata.artich

import net.halawata.artich.model.list.MediaList

interface ListActivityInterface {

    val list: MediaList

    fun updateList(content: String)

}
