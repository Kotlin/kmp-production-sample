package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import kotlinx.coroutines.launch

@Composable
fun MainFeed(
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

private sealed class Icons {
    object All : Icons()
    class FeedIcon(val feed: Feed) : Icons()
    object Edit : Icons()
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MainFeedBottomBar(
    feeds: List<Feed>,
    selectedFeed: Feed?,
    onFeedClick: (Feed?) -> Unit,
    onEditClick: () -> Unit
) {
    val items = buildList {
        add(Icons.All)
        addAll(feeds.map { Icons.FeedIcon(it) })
        add(Icons.Edit)
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        this.items(items) { item ->
            when (item) {
                is Icons.All -> FeedIcon(
                    feed = null,
                    isSelected = selectedFeed == null,
                    onClick = { onFeedClick(null) }
                )
                is Icons.FeedIcon -> FeedIcon(
                    feed = item.feed,
                    isSelected = selectedFeed == item.feed,
                    onClick = { onFeedClick(item.feed) }
                )
                is Icons.Edit -> EditIcon(onClick = onEditClick)
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}