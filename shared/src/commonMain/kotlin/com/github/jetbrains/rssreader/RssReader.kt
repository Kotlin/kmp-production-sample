package com.github.jetbrains.rssreader

import com.github.jetbrains.rssreader.datasource.network.FeedLoader

class RssReader {
    private val feedLoader = FeedLoader()

    fun getFeed(link: String) = feedLoader.getFeed(link)
}
