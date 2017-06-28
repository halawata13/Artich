package net.halawata.artich.model.list

import android.content.Context
import net.halawata.artich.entity.GNewsArticle
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.mute.GNewsMute
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.ArrayList

class GNewsList(): MediaList {

    override fun parse(content: String): ArrayList<GNewsArticle>? {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(content))

        var eventType = parser.eventType

        val articles = ArrayList<GNewsArticle>()
        var id: Long = 0
        var title = ""
        var url = ""
        var pubDate = ""

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

                if (eventType == XmlPullParser.START_TAG && parser.name == "pubDate") {
                    while (eventType != XmlPullParser.END_TAG || parser.name != "pubDate") {
                        if (eventType == XmlPullParser.TEXT) {
                            pubDate = parser.text
                        }

                        eventType = parser.next()
                    }
                }

                eventType = parser.next()
            }

            extractUrl(url)?.let {
                val article = GNewsArticle(
                        id = id++,
                        title = title,
                        url = it,
                        pubDate = pubDate
                )

                articles.add(article)
            }

            eventType = parser.next()
        }

        return articles
    }

    fun filter(data: ArrayList<GNewsArticle>, context: Context): ArrayList<GNewsArticle> {
        val filtered = ArrayList<GNewsArticle>()
        val helper = DatabaseHelper(context)
        val mute = GNewsMute(helper)
        val muteList = mute.getMuteList()

        data.forEach { item ->
            try {
                val url = URL(item.url)

                if (!muteList.contains(url.host)) {
                    filtered.add(item)
                }

            } catch (ex: MalformedURLException) {
                ex.printStackTrace()
            }
        }

        return filtered
    }

    fun extractUrl(urlString: String): String? {
        val regex = Regex("&url=(\\S+$)")
        val result = regex.find(urlString)

        return result?.groupValues?.get(1)
    }
}
