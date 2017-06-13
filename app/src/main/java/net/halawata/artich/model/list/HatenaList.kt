package net.halawata.artich.model.list

import net.halawata.artich.entity.HatenaArticle
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*

class HatenaList: MediaList {

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
                            pubDate = parser.text
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

        return articles
    }

}
