package com.github.jetbrains.rssreader

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.network.FeedParser
import com.github.jetbrains.rssreader.entity.Channel
import com.github.jetbrains.rssreader.entity.Feed
import com.github.jetbrains.rssreader.entity.Post
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.logging.*
import java.util.concurrent.TimeUnit

fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        createHttpClient(withLog),
        createFeedParser()
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}

private fun createHttpClient(withLog: Boolean) = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5, TimeUnit.SECONDS)
        }
    }
    install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "AndroidHttpClient", message = message)
            }
        }
    }
}

private fun createFeedParser() = object : FeedParser {
    override suspend fun parse(xml: String): Feed = Feed(
        Channel("ch_title", "ch_link", "ch_description"),
        listOf(
            Post("p_title", "p_link", "p_description", null, "p_date"),
            Post("p_title", "p_link", "p_description", null, "p_date"),
            Post("p_title", "p_link", "p_description", null, "p_date"),
            Post("p_title", "p_link", "p_description", null, "p_date"),
            Post("p_title", "p_link", "p_description", null, "p_date"),
            Post("p_title", "p_link", "p_description", null, "p_date"),
        )
    )
}