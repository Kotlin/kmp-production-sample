package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader

class RssReader internal constructor(
    private val feedLoader: FeedLoader
) {
    suspend fun getFeed(link: String) = feedLoader.getFeed(link)

    companion object
}
