package net.halawata.artich.model

import java.net.URLEncoder

object ApiUrlString {

    object Hatena {
        const val newEntry = "http://b.hatena.ne.jp/entrylist/it.rss"
        const val hotEntry = "http://b.hatena.ne.jp/hotentry/it.rss"

        fun get(keyword: String): String {
            val escaped = escape(keyword)
            return "http://b.hatena.ne.jp/keyword/$escaped?mode=rss&sort=count"
        }
    }

    object Qiita {
        const val newEntry = "https://qiita.com/api/v2/items"
        const val tagList = "https://qiita.com/api/v2/tags?sort=count&per_page=100"

        fun get(keyword: String): String {
            val escaped = escape(keyword)
            return "https://qiita.com/api/v2/tags/$escaped/items"
        }
    }

    object GNews {
        const val newEntry = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t"

        fun get(keyword: String): String {
            val escaped = escape(keyword)
            return "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&q=" + escaped
        }
    }

    fun escape(keyword: String): String = URLEncoder.encode(keyword, "UTF-8")
}
