package com.github.jetbrains.rssreader

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.HttpClient
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.datasource.storage.FeedStorage
import com.russhwolf.settings.PropertiesSettings
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.*

private val appModule = module {
    single { RssReader(get(), get(), Settings(setOf("https://blog.jetbrains.com/kotlin/feed/"))) }
    single<FeedStorage> {
        FeedStorage(
            PropertiesSettings(Properties()),
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            }
        )
    }
    single { FeedStore(get()) }
    single { FeedLoader(get()) }
    single { HttpClient(false) }
}

private fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "RSS reader",
    ) {
        RssReaderApp()
    }
}