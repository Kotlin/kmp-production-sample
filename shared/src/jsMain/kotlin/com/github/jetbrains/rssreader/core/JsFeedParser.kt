package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.parsing.DOMParser

internal class JsFeedParser : FeedParser {
    override suspend fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed {
        val parser = DOMParser()
        val feed = parser.parseFromString(xml, "text/xml")

        val image = feed.getElementsByTagName("image")[0]
        val imageUrl = image?.getElementsByTagName("url")?.get(0)?.childNodes?.get(0)?.nodeValue

        val posts = mutableListOf<Post>()
        val items = feed.getElementsByTagName("item")

        for (i in 0 until items.length) {
            // try default `featuredImage` node value
            var imageSourceUrl = elementNodeValue(items[i], "featuredImage")

            // try different formats of media thumbnails as url attribute
            var k = 0
            val mediaAttributes = arrayOf("media:thumbnail", "media:content")
            while(imageSourceUrl == "") {
                if (k > mediaAttributes.size) break
                val node = items[i]?.getElementsByTagName(mediaAttributes[k])?.get(0)
                imageSourceUrl = node?.getAttribute("url") ?: ""
                k++
            }

            posts.add(
                Post(
                    title = elementNodeValue(items[i], "title"),
                    link = elementNodeValue(items[i], "link"),
                    desc = elementNodeValue(items[i], "description"),
                    imageUrl = imageSourceUrl,
                    date = 100000 // TODO: convert as epoch millis for JS
                )
            )
        }

        return Feed(
            title = documentNodeValue(feed, "title"),
            link = documentNodeValue(feed, "link"),
            desc = documentNodeValue(feed, "description"),
            imageUrl = imageUrl,
            posts = posts,
            sourceUrl = sourceUrl,
            isDefault = isDefault
        )
    }

    private fun documentNodeValue(doc: Document, tagName: String) : String =
        doc.getElementsByTagName(tagName)[0]?.childNodes?.get(0)?.nodeValue ?: ""

    private fun elementNodeValue(el: Element?, tagName: String) : String =
        el?.getElementsByTagName(tagName)?.get(0)?.childNodes?.get(0)?.nodeValue ?: ""

}
