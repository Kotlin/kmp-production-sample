package com.github.jetbrains.rssreader.composeapp

internal fun _str(id: String) = when (id) {
    "app_name" -> "RSS reader"
    "rss_feed_url" -> "Rss feed url"
    "add" -> "Add"
    "remove" -> "Remove"
    "all" -> "All"
    else -> id
}