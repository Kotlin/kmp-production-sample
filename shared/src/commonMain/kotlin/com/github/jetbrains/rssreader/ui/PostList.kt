package com.github.jetbrains.rssreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.jetbrains.rssreader.domain.Item
import com.github.jetbrains.rssreader.domain.getImageUrl
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@Composable
fun PostList(
    modifier: Modifier,
    posts: List<Item>,
    listState: LazyListState,
    onClick: (Item) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                coroutineScope.launch {
                    listState.scrollBy(-delta)
                }
            },
        ),
        contentPadding = PaddingValues(16.dp),
        state = listState,
    ) {
        itemsIndexed(posts) { i, post ->
            PostItem(post) { onClick(post) }
            if (i != posts.size - 1) Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun PostItem(
    item: Item,
    onClick: () -> Unit
) {
    val padding = 16.dp
    Box {
        Card(
            shape = RoundedCornerShape(padding)
        ) {
            Column(
                modifier = Modifier.clickable(onClick = onClick)
            ) {
                Spacer(modifier = Modifier.size(padding))
                item.title?.let { title ->
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.headlineSmall,
                        text = title
                    )
                }
                item.getImageUrl()?.let { url ->
                    Spacer(modifier = Modifier.size(padding))
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.padding(start = padding, end = padding)
                    )
                }
                item.description?.let { desc ->
                    Spacer(modifier = Modifier.size(padding))
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        text = desc
                    )
                }
                Spacer(modifier = Modifier.size(padding))
                item.pubDate?.let { pubDate ->
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        text = pubDate
                    )
                    Spacer(modifier = Modifier.size(padding))
                }
            }
        }
    }
}