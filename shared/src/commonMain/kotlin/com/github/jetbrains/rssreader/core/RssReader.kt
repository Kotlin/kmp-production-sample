package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import com.github.jetbrains.rssreader.core.entity.Feed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RssReader internal constructor(
    private val feedLoader: FeedLoader,
    private val feedStorage: FeedStorage,
    private val feedSettings: FeedSettings = FeedSettings.basic()
) {
    @Throws(Exception::class)
    suspend fun getAllFeeds(
        forceUpdate: Boolean = false
    ): List<Feed> {
        var feeds = feedStorage.getAllFeeds()

        if (forceUpdate || feeds.isEmpty()) {
            val feedsUrls = if (feeds.isEmpty()) feedSettings.defaults else feeds.map { it.data.sourceUrl }
            feeds = feedsUrls.mapAsync { url ->
                getFeed(url).also {
                    feedStorage.saveFeed(it)
                }
            }
        }

        return feeds
    }

    @Throws(Exception::class)
    suspend fun addFeed(url: String) {
        val feed = getFeed(url)
        feedStorage.saveFeed(feed)
    }

    @Throws(Exception::class)
    suspend fun deleteFeed(url: String) {
        feedStorage.deleteFeed(url)
    }

    private suspend fun getFeed(url: String): Feed {
        return Feed(feedLoader.getFeedData(url), feedSettings.isRemovable(url))
    }

    private suspend fun <A, B> Iterable<A>.mapAsync(f: suspend (A) -> B): List<B> =
        coroutineScope { map { async { f(it) } }.awaitAll() }

    companion object
}
