package com.github.jetbrains.rssreader.core.datasource.network

import com.github.jetbrains.rssreader.core.entity.Feed
import io.ktor.http.*

interface FeedParser {
    suspend fun parse(sourceUrl: String, xml: String, isDefault: Boolean): Feed

    companion object {
        private val imgReg = Regex("<img[^>]+\\bsrc=[\"']([^\"']+)[\"']")
        private val htmlTag = Regex("<.+?>")
        private val blankLine = Regex("(?m)^[ \t]*\r?\n")

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

        internal fun cleanText(text: String?): String? =
            text?.replace(htmlTag, "")
                ?.replace(blankLine, "")
                ?.trim()

        internal fun cleanTextCompact(text: String?) = cleanText(text)?.take(300)

        internal fun pullPostImageUrl(postLink: String?, description: String?, content: String?): String? =
            postLink?.let { l ->
                description?.let { findImageUrl(l, it) }
                    ?: content?.let { findImageUrl(l, it) }
            }
    }
}