package com.github.jetbrains.rssreader.core

class FeedSettings(val defaults: List<String>) {

    fun isRemovable(feed: String) = defaults.contains(feed)

    companion object {
        fun basic() = FeedSettings(listOf("https://blog.jetbrains.com/kotlin/feed/"))
    }
}