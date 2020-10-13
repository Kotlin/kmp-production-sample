package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedParser
import com.github.jetbrains.rssreader.entity.Feed

internal class IosFeedParser : FeedParser {
    override suspend fun parse(sourceUrl: String, xml: String): Feed {
        //todo
        return Feed(
            "Dev title",
            sourceUrl,
            "dev description",
            null,
            emptyList(),
            sourceUrl
        )
    }
}