package net.halawata.artich.model

class ApiUrlString {

    class Hatena {
        companion object {
            val newEntry = "http://b.hatena.ne.jp/entrylist/it.rss"
            val hotEntry = "http://b.hatena.ne.jp/hotentry/it.rss"

            fun get(keyword: String): String {
                return "http://b.hatena.ne.jp/keyword/$keyword?mode=rss&sort=count"
            }
        }
    }

    class Qiita {
        companion object {
            val newEntry = "https://qiita.com/api/v2/items"

            fun get(keyword: String): String {
                return "https://qiita.com/api/v2/tags/$keyword/items"
            }
        }
    }

    class GNews {
        companion object {
            val newEntry = "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t"

            fun get(keyword: String): String {
                return "https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&q=" + keyword
            }
        }
    }
}
