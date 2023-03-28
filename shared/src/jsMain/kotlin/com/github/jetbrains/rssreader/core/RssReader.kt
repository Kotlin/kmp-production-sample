package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.DateParser
import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import com.russhwolf.settings.StorageSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.datetime.internal.JSJoda.DateTimeFormatter
import kotlinx.datetime.internal.JSJoda.ZonedDateTime
import kotlinx.serialization.json.Json

fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        JsHttpClient(withLog),
        FeedParser(JsDateParser())
    ),
    FeedStorage(
        StorageSettings(),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}

private fun JsHttpClient(withLog: Boolean) = HttpClient(Js) {
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "JsHttpClient", message = message)
            }
        }
    }
}

private class JsDateParser : DateParser {
    private val dateFormat = DateTimeFormatter.ofPattern(DateParser.DATE_FORMAT)
    override fun parse(date: String): Long =
        ZonedDateTime.parse(date, dateFormat).toEpochSecond().toLong()
}