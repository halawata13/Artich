package net.halawata.artich

interface AsyncNetworkTaskDelegate {

    fun success(content: String)

    fun fail(ex: Exception? = null)
}
