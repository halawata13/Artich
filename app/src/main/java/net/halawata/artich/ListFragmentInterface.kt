package net.halawata.artich

import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.list.MediaList

interface ListFragmentInterface {

    val list: MediaList

    fun reserve(urlString: String, title: String)

    fun update(urlString: String, title: String)

    fun request(urlString: String, title: String, onResponse: ((responseCode: Int?, content: String?) -> Unit)? = null, useCache: Boolean = true) {
        val asyncNetWorkTask = AsyncNetworkTask()
        asyncNetWorkTask.request(urlString, AsyncNetworkTask.Method.GET)

        asyncNetWorkTask.onResponse = onResponse
    }

    fun applyFilter()

    fun reload()
}
