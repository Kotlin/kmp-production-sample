package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.parsing.DOMParser

@ExperimentalJsExport
internal class JsFeedParser : FeedParser {
    override suspend fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed {
        val parser = DOMParser()
        val feed = parser.parseFromString(xml, "text/xml")

        val image = feed.getElementsByTagName("image")[0]
        val imageUrl = image?.getElementsByTagName("url")?.get(0)?.childNodes?.get(0)?.nodeValue

        val posts = mutableListOf<Post>()
        val items = feed.getElementsByTagName("item")

        for (i in 0 until items.length) {
            val title = elementNodeValue(items[i]!!, "title")
            val link = elementNodeValue(items[i]!!, "link")
            val description = elementNodeValue(items[i]!!, "description")
            val imageUrl = elementNodeValue(items[i]!!, "featuredImage")
            // TODO: convert as epoch millis for JS
            val date = elementNodeValue(items[i]!!, "date")

            posts.add(
                Post(
                    title,
                    link,
                    description,
                    imageUrl,
                    date = 100000
                )
            )
        }

        return Feed(
            title = nodeValue(feed, "title"),
            link = nodeValue(feed, "link"),
            desc = nodeValue(feed, "description"),
            imageUrl = imageUrl,
            posts = posts,
            sourceUrl = "https://blog.jetbrains.com/kotlin/feed/",
            isDefault = false
        )
    }

    private fun nodeValue(doc: Document, tagName: String) : String =
        doc.getElementsByTagName(tagName)[0]?.childNodes?.get(0)?.nodeValue ?: ""

    private fun elementNodeValue(doc: Element, tagName: String) : String =
        doc.getElementsByTagName(tagName)[0]?.childNodes?.get(0)?.nodeValue ?: ""
}
