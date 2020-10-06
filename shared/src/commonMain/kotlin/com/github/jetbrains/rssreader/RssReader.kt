package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.storage.FeedStorage
import com.github.jetbrains.rssreader.entity.Feed

class RssReader internal constructor(
    private val feedLoader: FeedLoader,
    private val feedStorage: FeedStorage
) {
    suspend fun getAllFeeds(
        forceUpdate: Boolean = false
    ): List<Feed> {
        var feeds = feedStorage.getAllFeeds()

        if (forceUpdate) {
            feeds = feeds.map { feed ->
                val new = feedLoader.getFeed(feed.sourceUrl)
                feedStorage.saveFeed(new)
                new
            }
        }

        //todo dev helper
        if (feeds.isEmpty()) {
            feeds = listOf(
                "https://blog.jetbrains.com/kotlin/feed/",
                "https://blog.elementary.io/feed.xml",
                "https://vas3k.ru/rss/"
            ).map { url ->
                val new = feedLoader.getFeed(url)
                feedStorage.saveFeed(new)
                new
            }
        }

        return feeds
    }

    suspend fun addFeed(url: String) {
        val feed = feedLoader.getFeed(url)
        feedStorage.saveFeed(feed)
    }

    suspend fun deleteFeed(url: String) {
        feedStorage.deleteFeed(url)
    }

    companion object
}
