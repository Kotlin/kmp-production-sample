package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.storage.FeedStorage
import com.github.jetbrains.rssreader.entity.Feed
import com.github.jetbrains.rssreader.entity.Post

class RssReader internal constructor(
    private val feedLoader: FeedLoader,
    private val feedStorage: FeedStorage
) {
    suspend fun getFeed(
        url: String,
        forceUpdate: Boolean = false
    ): Feed {
        val cached = feedStorage.getFeed(url)
        if (cached == null || forceUpdate) {
            val feed = feedLoader.getFeed(url)
            feedStorage.saveFeed(feed)
            return feed
        } else {
            return cached
        }
    }

    suspend fun getAllPosts(
        forceUpdate: Boolean = false
    ): List<Post> {
        var feeds = feedStorage.getAllFeeds()

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

        if (forceUpdate) {
            feeds = feeds.map { feed ->
                val new = feedLoader.getFeed(feed.sourceUrl)
                feedStorage.saveFeed(new)
                new
            }
        }
        return feeds.flatMap { it.posts }.sortedBy { it.date }
    }

    suspend fun getAllFeedUrls(): Set<String> =
        feedStorage.getAllFeeds().map { it.sourceUrl }.toSet()

    suspend fun addFeed(url: String) {
        val feed = feedLoader.getFeed(url)
        feedStorage.saveFeed(feed)
    }

    suspend fun deleteFeedUrl(url: String) {
        feedStorage.deleteFeed(url)
    }

    companion object
}
