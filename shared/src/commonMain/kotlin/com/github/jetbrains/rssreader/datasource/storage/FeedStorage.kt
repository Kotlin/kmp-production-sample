package com.github.jetbrains.rssreader.datasource.storage

import com.github.jetbrains.rssreader.entity.Feed

class FeedStorage {
    private val cache: MutableMap<String, Feed> = mutableMapOf()

    suspend fun getFeed(url: String): Feed? = cache[url]

    suspend fun saveFeed(url: String, feed: Feed) {
        cache[url] = feed
    }

    suspend fun deleteFeed(url: String) {
        cache.remove(url)
    }

    suspend fun getAllFeedUrls(): Set<String> = cache.keys
}