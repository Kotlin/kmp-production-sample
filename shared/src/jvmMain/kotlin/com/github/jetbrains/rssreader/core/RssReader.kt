package com.github.jetbrains.rssreader.core

import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

@OptIn(ExperimentalSettingsImplementation::class)
fun RssReader.Companion.create(withLog: Boolean) = RssReader(
    FeedLoader(
        JvmHttpClient(withLog),
        JvmFeedParser()
    ),
    FeedStorage(
        JvmPreferencesSettings(Preferences.userRoot()),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}