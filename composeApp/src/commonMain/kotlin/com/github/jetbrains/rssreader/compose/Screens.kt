package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainScreen : Screen, KoinComponent {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val state by store.observeState().collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val refreshState = rememberPullRefreshState(
            refreshing = state.progress,
            onRefresh = { store.dispatch(FeedAction.Refresh(true)) }
        )
        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }
        Box(modifier = Modifier.pullRefresh(refreshState)) {
            MainFeed(
                store = store,
                onPostClick = { post ->
                    openUrl(post.link)
                },
                onEditClick = {
                    navigator.push(FeedListScreen())
                }
            )
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = state.progress,
                state = refreshState,
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