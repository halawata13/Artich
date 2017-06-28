package net.halawata.artich.model.list

import kotlin.collections.ArrayList

interface MediaList {

    fun parse(content: String): ArrayList<*>?
}
