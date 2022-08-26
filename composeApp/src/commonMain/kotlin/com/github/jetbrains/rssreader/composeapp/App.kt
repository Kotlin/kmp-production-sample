package com.github.jetbrains.rssreader.composeapp

import androidx.compose.runtime.Composable
import com.github.jetbrains.rssreader.app.FeedStore

expect val AppStore: FeedStore

@Composable
fun App() {
    AppTheme {
        MainScreen.Content()
    }
}
