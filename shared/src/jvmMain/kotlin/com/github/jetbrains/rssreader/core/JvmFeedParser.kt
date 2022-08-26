package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser.Companion.cleanTextCompact
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser.Companion.pullPostImageUrl
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import io.github.aakira.napier.Napier
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import java.io.StringReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.SAXParserFactory

internal class JvmFeedParser : FeedParser {
    override suspend fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed = withContext(Dispatchers.IO) {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        val handler = SaxFeedHandler(sourceUrl, isDefault)

        parser.parse(InputSource(StringReader(xml)), handler)

        return@withContext handler.getFeed()
    }

    private class SaxFeedHandler(
        private val sourceUrl: String,
        private val isDefault: Boolean
    ) : DefaultHandler() {
        private val dateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)

        private var title: String? = null
        private var link: String? = null
        private var description: String? = null
        private var imageUrl: String? = null
        private val posts = mutableListOf<Post>()

        private var imgProcess = false
        private var postProcess = false

        private var pTitle: String? = null
        private var pLink: String? = null
        private var pDescription: String? = null
        private var pContent: String? = null
        private var pDate: String? = null

        private var text: String = ""

        fun getFeed() = Feed(title!!, link!!, description!!, imageUrl, posts, sourceUrl, isDefault)

        override fun startElement(
            uri: String?,
            localName: String?,
            qName: String?,
            attributes: Attributes?
        ) {
            when (qName) {
                "item" -> postProcess = true
                "image" -> imgProcess = true
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String?) {
            if (postProcess) {
                when (qName) {
                    "title" -> pTitle = text
                    "link" -> pLink = text
                    "description" -> pDescription = text
                    "content:encoded" -> pContent = text
                    "pubDate" -> pDate = text
                    "item" -> {
                        val dateLong: Long = pDate?.let {
                            try {
                                ZonedDateTime.parse(pDate, dateFormat).toEpochSecond() * 1000
                            } catch (e: Throwable) {
                                Napier.e("Parse date error: ${e.message}")
                                null
                            }
                        } ?: System.currentTimeMillis()
                        val post = Post(
                            pTitle ?: title ?: "",
                            link,
                            cleanTextCompact(pDescription),
                            pullPostImageUrl(pLink, pDescription, pContent),
                            dateLong
                        )
                        posts.add(post)
                        postProcess = false
                    }
                }
            } else {
                when (qName) {
                    "title" -> title = text
                    "link" -> link = text
                    "description" -> description = text
                    "image" -> imgProcess = false
                    "url" -> {
                        if (imgProcess) imageUrl = text
                    }
                }
            }
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
            text = String(ch, start, length)
        }
    }
}