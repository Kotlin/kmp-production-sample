package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.storage.FeedStorage
import com.github.jetbrains.rssreader.entity.Feed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RssReader internal constructor(
    private val feedLoader: FeedLoader,
    private val feedStorage: FeedStorage
) {
    @Throws(Exception::class)
    suspend fun getAllFeeds(
        forceUpdate: Boolean = false
    ): List<Feed> {
        var feeds = feedStorage.getAllFeeds()

        if (forceUpdate) {
            feeds = feeds.mapAsync { feed ->
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
            ).mapAsync { url ->
                val new = feedLoader.getFeed(url)
                feedStorage.saveFeed(new)
                new
            }
        }

        return feeds
    }

    @Throws(Exception::class)
    suspend fun addFeed(url: String) {
        val feed = feedLoader.getFeed(url)
        feedStorage.saveFeed(feed)
    }

    @Throws(Exception::class)
    suspend fun deleteFeed(url: String) {
        feedStorage.deleteFeed(url)
    }

    private suspend fun <A, B> Iterable<A>.mapAsync(f: suspend (A) -> B): List<B> =
        coroutineScope { map { async { f(it) } }.awaitAll() }

    companion object
}
