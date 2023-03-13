package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.DateParser
import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import com.russhwolf.settings.NSUserDefaultsSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.NSUserDefaults
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeZoneForSecondsFromGMT

fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        IosHttpClient(withLog),
        FeedParser(IosDateParser())
    ),
    FeedStorage(
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults()),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}

private fun IosHttpClient(withLog: Boolean) = HttpClient(Darwin) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "IosHttpClient", message = message)
            }
        }
    }
}

private class IosDateParser : DateParser {
    private val dateFormatter = NSDateFormatter().apply {
        setLocale(NSLocale("en_US_POSIX"))
        setTimeZone(NSTimeZone.timeZoneForSecondsFromGMT(0))
        dateFormat = DateParser.DATE_FORMAT
    }

    override fun parse(date: String): Long =
        dateFormatter.dateFromString(date)?.timeIntervalSince1970?.toLong() ?: 0L
}