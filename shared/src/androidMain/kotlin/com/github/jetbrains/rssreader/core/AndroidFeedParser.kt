package com.github.jetbrains.rssreader.core

import android.util.Xml
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import io.github.aakira.napier.Napier
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

internal class AndroidFeedParser : FeedParser {
    private val dateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)

    override suspend fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed = withContext(Dispatchers.IO) {
        val parser = Xml.newPullParser().apply {
            setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        }

        var feed: Feed

        xml.reader().use { reader ->
            parser.setInput(reader)

            var tag = parser.nextTag()
            while (tag != XmlPullParser.START_TAG && parser.name != "rss") {
                skip(parser)
                tag = parser.next()
            }
            parser.nextTag()

            feed = readFeed(sourceUrl, parser, isDefault)
        }

        return@withContext feed
    }

    private fun readFeed(sourceUrl: String, parser: XmlPullParser, isDefault: Boolean): Feed {
        parser.require(XmlPullParser.START_TAG, null, "channel")

        var title: String? = null
        var link: String? = null
        var description: String? = null
        var imageUrl: String? = null
        val posts = mutableListOf<Post>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "title" -> title = readTagText("title", parser)
                "link" -> link = readTagText("link", parser)
                "description" -> description = readTagText("description", parser)
                "image" -> imageUrl = readImageUrl(parser)
                "item" -> posts.add(readPost(title!!, parser))
                else -> skip(parser)
            }
        }

        return Feed(title!!, link!!, description!!, imageUrl, posts, sourceUrl, isDefault)
    }

    private fun readImageUrl(parser: XmlPullParser): String? {
        parser.require(XmlPullParser.START_TAG, null, "image")

        var url: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "url" -> url = readTagText("url", parser)
                else -> skip(parser)
            }
        }

        return url
    }

    private fun readPost(feedTitle: String, parser: XmlPullParser): Post {
        parser.require(XmlPullParser.START_TAG, null, "item")

        var title: String? = null
        var link: String? = null
        var description: String? = null
        var date: String? = null

        var content: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "title" -> title = readTagText("title", parser)
                "link" -> link = readTagText("link", parser)
                "description" -> description = readTagText("description", parser)
                "content:encoded" -> content = readTagText("content:encoded", parser)
                "pubDate" -> date = readTagText("pubDate", parser)
                else -> skip(parser)
            }
        }

        val dateLong: Long = date?.let {
            try {
                ZonedDateTime.parse(date, dateFormat).toEpochSecond() * 1000
            } catch (e: Throwable) {
                Napier.e("Parse date error: ${e.message}")
                null
            }
        } ?: System.currentTimeMillis()

        return Post(
            title ?: feedTitle,
            link,
            FeedParser.cleanTextCompact(description),
            FeedParser.pullPostImageUrl(link, description, content),
            dateLong
        )
    }

    private fun readTagText(tagName: String, parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, tagName)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tagName)
        return title
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, null, null)
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}