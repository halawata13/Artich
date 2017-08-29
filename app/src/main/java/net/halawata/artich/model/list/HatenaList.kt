package net.halawata.artich.model.list

import android.content.Context
import net.halawata.artich.entity.HatenaArticle
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.Log
import net.halawata.artich.model.mute.HatenaMute
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.ArrayList

class HatenaList(): MediaList {

    override val dateFormat = "yyyy-MM-dd'T'HH:mm:ssz"

    override fun parse(content: String): ArrayList<HatenaArticle>? {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(content))

        var eventType = parser.eventType

        val articles = ArrayList<HatenaArticle>()
        var id: Long = 0
        var title = ""
        var url = ""
        var pubDate = ""

        try {
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // item タグ開始が来るまでスキップ
                if (eventType != XmlPullParser.START_TAG || parser.name != "item") {
                    eventType = parser.next()
                    continue
                }

                while (eventType != XmlPullParser.END_TAG || parser.name != "item") {
                    if (eventType == XmlPullParser.START_TAG && parser.name == "title") {
                        while (eventType != XmlPullParser.END_TAG || parser.name != "title") {
                            if (eventType == XmlPullParser.TEXT) {
                                title = parser.text
                            }

                            eventType = parser.next()
                        }
                    }

                    if (eventType == XmlPullParser.START_TAG && parser.name == "link") {
                        while (eventType != XmlPullParser.END_TAG || parser.name != "link") {
                            if (eventType == XmlPullParser.TEXT) {
                                url = parser.text
                            }

                            eventType = parser.next()
                        }
                    }

                    if (eventType == XmlPullParser.START_TAG && parser.name == "dc:date") {
                        while (eventType != XmlPullParser.END_TAG || parser.name != "dc:date") {
                            if (eventType == XmlPullParser.TEXT) {
                                pubDate = formatDate(parser.text) ?: ""
                            }

                            eventType = parser.next()
                        }
                    }

                    eventType = parser.next()
                }

                val article = HatenaArticle(
                        id = id++,
                        title = title,
                        url = url,
                        pubDate = pubDate
                )

                articles.add(article)

                eventType = parser.next()
            }

        } catch (ex: Exception) {
            Log.e(ex.message)
        }

        articles.sortByDescending { article -> article.pubDate }

        return articles
    }

    fun filter(data: ArrayList<HatenaArticle>, context: Context): ArrayList<HatenaArticle> {
        val filtered = ArrayList<HatenaArticle>()
        val helper = DatabaseHelper(context)
        val mute = HatenaMute(helper)
        val muteList = mute.getMuteList()

        data.forEach { item ->
            try {
                val url = URL(item.url)

                if (!muteList.contains(url.host)) {
                    filtered.add(item)
                }

            } catch (ex: MalformedURLException) {
                Log.e(ex.message)
            }
        }

        return filtered
    }
}
