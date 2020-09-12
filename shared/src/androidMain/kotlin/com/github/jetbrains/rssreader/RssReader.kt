package com.github.jetbrains.rssreader

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.github.jetbrains.rssreader.datasource.network.FeedLoader

fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        AndroidHttpClient(withLog),
        AndroidFeedParser()
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}