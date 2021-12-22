package com.github.jetbrains.rssreader.androidApp

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.sharedui.FeedList
import com.github.jetbrains.rssreader.sharedui.MainFeed
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val state = store.observeState().collectAsState()
        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.value.progress),
            indicatorPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.systemBars),
            clipIndicatorToPadding = false,
            indicator = { indicatorState, refreshTriggerDistance ->
                SwipeRefreshIndicator(
                    state = indicatorState,
                    refreshTriggerDistance = refreshTriggerDistance,
                    scale = true //https://github.com/google/accompanist/issues/572
                )
            },
            onRefresh = { store.dispatch(FeedAction.Refresh(true)) }
        ) {
            MainFeed(
                store = store,
                onPostClick = { post ->
                    post.link?.let { url ->
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                },
                onEditClick = {
                    navigator.push(FeedListScreen())
                }
            )
        }
    }
}

class FeedListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        FeedList(store = store)
    }
}