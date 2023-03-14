package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.images.MRImages
import io.github.skeptick.libres.compose.painterResource

@Composable
internal fun FeedList(store: FeedStore) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = store.observeState().collectAsState()

        val addFeedDialog = AddFeedDialog { url -> store.dispatch(FeedAction.Add(url)) }
        val deleteFeedDialog = DeleteFeedDialog { url -> store.dispatch(FeedAction.Delete(url)) }

        FeedItemList(feeds = state.value.feeds) {
            deleteFeedDialog.show(it)
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding(),
            onClick = { addFeedDialog.show(Unit) }
        ) {
            Image(
                painter = painterResource(MRImages.ic_add),
                modifier = Modifier.align(Alignment.Center),
                contentDescription = null
            )
        }
    }
}

@Composable
internal fun FeedItemList(
    feeds: List<Feed>,
    onClick: (Feed) -> Unit
) {
    LazyColumn {
        itemsIndexed(feeds) { i, feed ->
            FeedItem(feed) { onClick(feed) }
        }
    }
}

@Composable
internal fun FeedItem(
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
                style = MaterialTheme.typography.body1,
                text = feed.title
            )
            Text(
                style = MaterialTheme.typography.body2,
                text = feed.desc
            )
        }
    }
}
