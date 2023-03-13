package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.DateParser
import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import com.russhwolf.settings.PreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        JvmHttpClient(withLog),
        FeedParser(JvmDateParser())
    ),
    FeedStorage(
        PreferencesSettings.Factory().create("test"),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}

private fun JvmHttpClient(withLog: Boolean) = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5, TimeUnit.SECONDS)
        }
    }
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "AndroidHttpClient", message = message)
            }
        }
    }
}

@Suppress("NewApi")
private class JvmDateParser : DateParser {
    private val dateFormat = DateTimeFormatter.ofPattern(DateParser.DATE_FORMAT, Locale.US)
    override fun parse(date: String): Long =
        ZonedDateTime.parse(date, dateFormat).toEpochSecond()

}