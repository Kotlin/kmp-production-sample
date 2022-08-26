package com.github.jetbrains.rssreader.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore

expect val AppStore: FeedStore

@Composable
fun App() = AppTheme {
    LaunchedEffect(Unit) {
        AppStore.dispatch(FeedAction.Refresh(false))
    }
    MainFeed(
        store = AppStore,
        onPostClick = { post ->
            post.link?.let { url -> }
        },
        onEditClick = {}
    )
}
