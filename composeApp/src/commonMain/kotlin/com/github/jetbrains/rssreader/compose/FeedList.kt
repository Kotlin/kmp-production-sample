package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.entity.Feed
import org.jetbrains.compose.resources.vectorResource
import rssreader.composeapp.generated.resources.Res

@Composable
fun FeedList(store: FeedStore) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = store.observeState().collectAsState()
        val showAddDialog = remember { mutableStateOf(false) }
        val feedForDelete = remember<MutableState<Feed?>> { mutableStateOf(null) }
        FeedItemList(feeds = state.value.feeds) {
            feedForDelete.value = it
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding(),
            onClick = { showAddDialog.value = true }
        ) {
            Image(
                imageVector = vectorResource(Res.drawable.ic_add),
                modifier = Modifier.align(Alignment.Center),
                contentDescription = null
            )
        }
        if (showAddDialog.value) {
            AddFeedDialog(
                onAdd = {
                    store.dispatch(FeedAction.Add(it))
                    showAddDialog.value = false
                },
                onDismiss = {
                    showAddDialog.value = false
                }
            )
        }
        feedForDelete.value?.let { feed ->
            DeleteFeedDialog(
                feed = feed,
                onDelete = {
                    store.dispatch(FeedAction.Delete(feed.sourceUrl))
                    feedForDelete.value = null
                },
                onDismiss = {
                    feedForDelete.value = null
                }
            )
        }
    }
}

@Composable
fun FeedItemList(
    feeds: List<Feed>,
    onClick: (Feed) -> Unit
) {
    LazyColumn {
        itemsIndexed(feeds) { i, feed ->
            if (i == 0) Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            FeedItem(feed) { onClick(feed) }
        }
    }
}

@Composable
fun FeedItem(
    feed: Feed,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .clickable(onClick = onClick, enabled = !feed.isDefault)
            .padding(16.dp)
    ) {
        FeedIcon(feed = feed)
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = feed.title
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = feed.desc
            )
        }
    }
}
