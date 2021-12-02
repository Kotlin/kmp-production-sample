package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.entity.Post
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    store: FeedStore,
    onPostClick: (Post) -> Unit,
    onEditClick: () -> Unit,
) {
    val state = store.observeState().collectAsState()
    val posts = remember(state.value.feeds, state.value.selectedFeed) {
        (state.value.selectedFeed?.posts ?: state.value.feeds.flatMap { it.posts })
            .sortedByDescending { it.date }
    }
    Column {
            val coroutineScope = rememberCoroutineScope()
            val listState = rememberLazyListState()
            PostList(
                modifier = Modifier.weight(1f),
                posts = posts,
                listState = listState
            ) { post -> onPostClick(post) }
            MainFeedBottomBar(
                feeds = state.value.feeds,
                selectedFeed = state.value.selectedFeed,
                onFeedClick = { feed ->
                    coroutineScope.launch { listState.scrollToItem(0) }
                    store.dispatch(FeedAction.SelectFeed(feed))
                },
                onEditClick = onEditClick
            )
            Spacer(
                Modifier
                    .systemNavigationBarsHeight()
                    .fillMaxWidth()
            )
    }
}