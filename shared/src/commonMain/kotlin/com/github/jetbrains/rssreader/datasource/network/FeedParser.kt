package com.github.jetbrains.rssreader.datasource.network

import com.github.jetbrains.rssreader.entity.Feed
import io.ktor.http.*

abstract class FeedParser {
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

    internal fun clean(description: String?) : String? =
        description?.replace(htmlTag, "")
            ?.replace(blankLine, "")
            ?.trim()
            ?.take(300)

    internal fun pullPostImageUrl(postLink: String?, description: String?, content: String?): String? =
        postLink?.let { l ->
            description?.let { findImageUrl(l, it) }
                ?: content?.let { findImageUrl(l, it) }
        }

    abstract suspend fun parse(sourceUrl: String, xml: String): Feed
}