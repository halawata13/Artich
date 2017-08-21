package net.halawata.artich.model

object ApiUrlString {

    object Hatena {
        val newEntry = "http://b.hatena.ne.jp/entrylist/it.rss"
        val hotEntry = "http://b.hatena.ne.jp/hotentry/it.rss"

        fun get(keyword: String): String = "http://b.hatena.ne.jp/keyword/$keyword?mode=rss&sort=count"
    }

    object Qiita {
        val newEntry = "https://qiita.com/api/v2/items"
        val tagList = "https://qiita.com/api/v2/tags?sort=count&per_page=100"

        fun get(keyword: String): String = "https://qiita.com/api/v2/tags/$keyword/items"
    }

    object GNews {
        val newEntry = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t"

        fun get(keyword: String): String = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&q=" + keyword
    }
}
