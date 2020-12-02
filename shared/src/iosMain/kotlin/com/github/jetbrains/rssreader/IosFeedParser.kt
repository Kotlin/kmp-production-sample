package com.github.jetbrains.rssreader

import com.github.aakira.napier.Napier
import com.github.jetbrains.rssreader.datasource.network.FeedParser
import com.github.jetbrains.rssreader.entity.Feed
import com.github.jetbrains.rssreader.entity.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class IosFeedParser : FeedParser {
    override suspend fun parse(sourceUrl: String, xml: String): Feed =
        withContext(Dispatchers.Default) {
            suspendCoroutine { continuation ->
                Napier.v(tag = "IosFeedParser", message = "Start parse $sourceUrl")
                NSXMLParser((xml as NSString).dataUsingEncoding(NSUTF8StringEncoding)!!).apply {
                    delegate = RssFeedParser(sourceUrl) { continuation.resume(it) }
                }.parse()
            }
        }

    private class RssFeedParser(
        private val sourceUrl: String,
        private val onEnd: (Feed) -> Unit
    ) : NSObject(), NSXMLParserDelegateProtocol {
        private val posts = mutableListOf<Post>()

        private var currentChannelData: MutableMap<String, String> = mutableMapOf()
        private var currentItemData: MutableMap<String, String> = mutableMapOf()
        private var currentData: MutableMap<String, String>? = null
        private var currentElement: String? = null

        private val dateFormatter = NSDateFormatter().apply {
            dateFormat = "E, d MMM yyyy HH:mm:ss Z"
        }

        override fun parser(
            parser: NSXMLParser,
            didStartElement: String,
            namespaceURI: String?,
            qualifiedName: String?,
            attributes: Map<Any?, *>
        ) {
            currentElement = didStartElement
            currentData = when (currentElement) {
                "channel" -> currentChannelData
                "item" -> currentItemData
                else -> currentData
            }
        }

        override fun parser(parser: NSXMLParser, foundCharacters: String) {
            val currentElement = currentElement ?: return
            val currentData = currentData ?: return
            currentData[currentElement] = (currentData[currentElement] ?: "") + foundCharacters
        }

        override fun parser(
            parser: NSXMLParser,
            didEndElement: String,
            namespaceURI: String?,
            qualifiedName: String?
        ) {
            if (didEndElement == "item") {
                posts.add(Post.withMap(currentItemData))
                currentItemData.clear()
            }
        }

        override fun parserDidEndDocument(parser: NSXMLParser) {
            Napier.v(tag = "IosFeedParser", message = "end parse $sourceUrl")
            onEnd(Feed.withMap(currentChannelData, posts, sourceUrl))
        }

        private fun Post.Companion.withMap(rssMap: Map<String, String>): Post {
            val pubDate = rssMap["pubDate"]
            val date =
                if (pubDate != null)
                    dateFormatter.dateFromString(pubDate.trim())?.timeIntervalSince1970
                else
                    null
            val link = rssMap["link"]
            val description = rssMap["description"]
            val content = rssMap["content:encoded"]
            return Post(
                FeedParser.cleanText(rssMap["title"])!!,
                FeedParser.cleanText(link),
                FeedParser.cleanTextCompact(description),
                FeedParser.pullPostImageUrl(link, description, content),
                date?.toLong() ?: 0
            )
        }

        private fun Feed.Companion.withMap(
            rssMap: Map<String, String>,
            posts: List<Post>,
            sourceUrl: String
        ) = Feed(
            rssMap["title"]!!,
            rssMap["link"]!!,
            rssMap["description"]!!,
            null,
            posts,
            sourceUrl
        )
    }
}




