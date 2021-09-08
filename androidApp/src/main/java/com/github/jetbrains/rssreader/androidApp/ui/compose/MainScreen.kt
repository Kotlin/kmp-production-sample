package com.github.jetbrains.rssreader.androidApp.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val state = store.observeState().collectAsState()
        val posts = remember(state.value.feeds, state.value.selectedFeed) {
            (state.value.selectedFeed?.posts ?: state.value.feeds.flatMap { it.posts })
                .sortedByDescending { it.date }
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
            Column {
                val coroutineScope = rememberCoroutineScope()
                val listState = rememberLazyListState()
                PostList(
                    modifier = Modifier.weight(1f),
                    posts = posts,
                    listState = listState
                ) { post ->
                    post.link?.let { url ->
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                }
                MainFeedBottomBar(
                    feeds = state.value.feeds,
                    selectedFeed = state.value.selectedFeed,
                    onFeedClick = { feed ->
                        coroutineScope.launch { listState.scrollToItem(0) }
                        store.dispatch(FeedAction.SelectFeed(feed))
                    },
                    onEditClick = { navigator.push(FeedListScreen()) }
                )
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}