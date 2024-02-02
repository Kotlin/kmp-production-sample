package com.github.jetbrains.rssreader.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }
        MainFeed(
            store = store,
            onPostClick = { post -> openUrl(post.link) },
            onEditClick = {
                navigator.push(FeedListScreen())
            }
        )
    }
}

class FeedListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        FeedList(store = store)
    }
}