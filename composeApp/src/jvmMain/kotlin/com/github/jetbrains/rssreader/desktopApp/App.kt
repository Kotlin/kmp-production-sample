package com.github.jetbrains.rssreader.desktopApp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import com.github.jetbrains.rssreader.sharedui.AppTheme
import com.github.jetbrains.rssreader.sharedui.MainScreen

val rssReader = RssReader.create(true)
val store = FeedStore(rssReader).also { it.dispatch(FeedAction.Refresh(true)) }

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RSS reader",
        state = rememberWindowState(width = 400.dp, height = 800.dp)
    ) {
        AppTheme {
            MainScreen(store, {}, {})
        }
    }
}