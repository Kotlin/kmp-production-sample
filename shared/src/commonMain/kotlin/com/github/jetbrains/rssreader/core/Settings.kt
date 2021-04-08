package com.github.jetbrains.rssreader.core

class Settings(val defaultFeedUrls: Set<String>) {
    fun isDefault(feedUrl: String) = defaultFeedUrls.contains(feedUrl)
}