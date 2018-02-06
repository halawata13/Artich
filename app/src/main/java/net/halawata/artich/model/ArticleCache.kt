package net.halawata.artich.model

import android.content.Context
import android.net.http.HttpResponseCache
import java.io.File
import java.io.IOException

object ArticleCache {

    private const val cacheSize: Long = 10 * 1024 * 1024

    fun init(context: Context) {

        try {
            val cacheDir = File(context.cacheDir, "http")
            HttpResponseCache.install(cacheDir, cacheSize)

        } catch (ex: IOException) {
            Log.e(ex.message)
        }
    }

    fun clear() {
        HttpResponseCache.getInstalled()?.flush()
    }
}
