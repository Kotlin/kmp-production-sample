package com.github.jetbrains.rssreader.datasource.network

import com.github.jetbrains.rssreader.entity.Feed

interface FeedParser {
    suspend fun parse(sourceUrl: String, xml: String): Feed
}