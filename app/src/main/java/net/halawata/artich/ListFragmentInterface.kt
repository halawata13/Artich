package net.halawata.artich

import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.list.MediaList

interface ListFragmentInterface {

    val list: MediaList

    var selectedTitle: String

    var selectedUrlString: String

    fun reserve(urlString: String, title: String)

    fun update(urlString: String, title: String, useCache: Boolean = true)

    fun request(urlString: String, title: String, onResponse: ((responseCode: Int?, content: String?) -> Unit)? = null, useCache: Boolean = true) {
        val asyncNetWorkTask = AsyncNetworkTask()
        asyncNetWorkTask.request(urlString, AsyncNetworkTask.Method.GET, null, useCache)

        asyncNetWorkTask.onResponse = onResponse
    }

    fun applyFilter()

    fun reload(useCache: Boolean = true)
}
