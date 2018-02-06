package net.halawata.artich.model

object ApiUrlString {

    object Hatena {
        const val newEntry = "http://b.hatena.ne.jp/entrylist/it.rss"
        const val hotEntry = "http://b.hatena.ne.jp/hotentry/it.rss"

        fun get(keyword: String): String = "http://b.hatena.ne.jp/keyword/$keyword?mode=rss&sort=count"
    }

    object Qiita {
        const val newEntry = "https://qiita.com/api/v2/items"
        const val tagList = "https://qiita.com/api/v2/tags?sort=count&per_page=100"

        fun get(keyword: String): String = "https://qiita.com/api/v2/tags/$keyword/items"
    }

    object GNews {
        const val newEntry = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t"

        fun get(keyword: String): String = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&q=" + keyword
    }
}
