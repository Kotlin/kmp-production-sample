package com.github.jetbrains.rssreader.composeapp

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import com.seiko.imageloader.ImageLoaderBuilder

actual fun buildImageLoader() = ImageLoaderBuilder().build()
actual val AppStore: FeedStore = FeedStore(RssReader.create(true))

fun main() = singleWindowApplication(
    title = "RSS Reader",
    state = WindowState(size = DpSize(400.dp, 800.dp))
) {
    App()
}