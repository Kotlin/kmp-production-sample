package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.storage.FeedStorage
import com.github.jetbrains.rssreader.entity.Feed

class RssReader internal constructor(
    private val feedLoader: FeedLoader,
    private val feedStorage: FeedStorage
) {
    suspend fun getFeed(
        url: String,
        forceLoad: Boolean = false
    ): Feed {
        val cached = feedStorage.getFeed(url)
        if (cached == null || forceLoad) {
            val feed = feedLoader.getFeed(url)
            feedStorage.saveFeed(url, feed)
            return feed
        } else {
            return cached
        }
    }

    suspend fun getAllFeedUrls(): Set<String> =
        feedStorage.getAllFeedUrls()

    suspend fun addFeed(url: String) {
        val feed = feedLoader.getFeed(url)
        feedStorage.saveFeed(url, feed)
    }

    suspend fun deleteFeedUrl(url: String) {
        feedStorage.deleteFeed(url)
    }

    companion object
}
