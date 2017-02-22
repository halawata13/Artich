package net.halawata.artich.model.list

import java.util.*

interface MediaList {

    fun parse(content: String): ArrayList<*>?

}
