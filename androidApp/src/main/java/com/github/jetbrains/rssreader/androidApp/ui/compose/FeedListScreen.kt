package com.github.jetbrains.rssreader.androidApp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.entity.Feed
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun FeedListScreen(
    store: FeedStore
) {
    AppTheme {
        ProvideWindowInsets {
            Box {
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
                        .navigationBarsWithImePadding(),
                    onClick = { showAddDialog.value = true }
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
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
    }
}