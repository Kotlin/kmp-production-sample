package com.github.jetbrains.rssreader.core.datasource.network

import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import com.github.jetbrains.rssreader.core.parseDateTimeString
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlCData
import nl.adaptivity.xmlutil.serialization.XmlConfig
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

internal class FeedParser {
    private val parser = XML {
        defaultPolicy {
            repairNamespaces = true
            isCollectingNSAttributes = true
            unknownChildHandler = XmlConfig.IGNORING_UNKNOWN_CHILD_HANDLER
        }
    }

    fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed {
        val rss = parser.decodeFromString<XmlRss>(xml)
        val channel = rss.channel.first()
        return Feed(
            channel.title,
            channel.link,
            channel.description,
            channel.image.url,
            channel.items.map { item ->
                Post(
                    item.title ?: channel.title,
                    item.link,
                    cleanTextCompact(item.description),
                    pullPostImageUrl(item.link, item.description, item.encoded),
                    parseDateTimeString(item.pubDate)
                )
            },
            sourceUrl,
            isDefault
        )
    }

    private fun findImageUrl(ownerLink: String, text: String): String? =
        imgReg.find(text)?.value?.let { v ->
            val i = v.indexOf("src=") + 5 //after src="
            val url = v.substring(i, v.length - 1)
            if (url.startsWith("http")) url else {
                URLBuilder(ownerLink).apply {
                    encodedPath = url
                }.buildString()
            }
        }

    private fun cleanText(text: String?): String? =
        text?.replace(htmlTag, "")
            ?.replace(blankLine, "")
            ?.trim()

    private fun cleanTextCompact(text: String?) = cleanText(text)?.take(300)

    private fun pullPostImageUrl(postLink: String?, description: String?, content: String?): String? =
        postLink?.let { l ->
            description?.let { findImageUrl(l, it) }
                ?: content?.let { findImageUrl(l, it) }
        }

    companion object {
        private val imgReg = Regex("<img[^>]+\\bsrc=[\"']([^\"']+)[\"']")
        private val htmlTag = Regex("<.+?>")
        private val blankLine = Regex("(?m)^[ \t]*\r?\n")
    }

    @Serializable
    @XmlSerialName("rss", "", "")
    private class XmlRss(
        @XmlElement(true) val channel: List<XmlChannel>
    )

    @Serializable
    @XmlSerialName("channel", "", "")
    private class XmlChannel(
        @XmlElement(true) val title: String,
        @XmlElement(true) val link: String,
        @XmlElement(true) val description: String,
        @XmlElement(true) val image: XmlChannelImage,
        @XmlElement(true) val items: List<XmlChannelItem>
    )

    @Serializable
    @XmlSerialName("image", "", "")
    private class XmlChannelImage(
        @XmlElement(true) val url: String
    )

    @Serializable
    @XmlSerialName("item", "", "")
    private class XmlChannelItem(
        @XmlElement(true) val title: String?,
        @XmlElement(true) val link: String?,
        @XmlElement(true) val description: String?,
        @XmlElement(true) val pubDate: String,
        @XmlSerialName("encoded", "http://purl.org/rss/1.0/modules/content/", "")
        @XmlCData(true)
        @XmlElement(true)
        val encoded: String?
    )
}