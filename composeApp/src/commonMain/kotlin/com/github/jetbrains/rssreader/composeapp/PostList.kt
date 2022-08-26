package com.github.jetbrains.rssreader.composeapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.core.entity.Post
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PostList(
    modifier: Modifier,
    posts: List<Post>,
    listState: LazyListState,
    onClick: (Post) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        state = listState
    ) {
        itemsIndexed(posts) { i, post ->
            PostItem(post) { onClick(post) }
            if (i != posts.size - 1) Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun PostItem(
    item: Post,
    onClick: () -> Unit
) {
    val padding = 16.dp
    Box {
        Card(
            elevation = 16.dp,
            shape = RoundedCornerShape(padding)
        ) {
            Column(
                modifier = Modifier.clickable(onClick = onClick)
            ) {
                Spacer(modifier = Modifier.size(padding))
                Text(
                    modifier = Modifier.padding(start = padding, end = padding),
                    style = MaterialTheme.typography.h6,
                    text = item.title
                )
                item.imageUrl?.let { url ->
                    Spacer(modifier = Modifier.size(padding))
                    RemoteImage(
                        url,
                        modifier = Modifier.height(180.dp).fillMaxWidth(),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )
                }
                item.desc?.let { desc ->
                    Spacer(modifier = Modifier.size(padding))
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.body1,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        text = desc
                    )
                }
                Spacer(modifier = Modifier.size(padding))
                Text(
                    modifier = Modifier.padding(start = padding, end = padding),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    text = Instant.fromEpochMilliseconds(item.date).format()
                )
                Spacer(modifier = Modifier.size(padding))
            }
        }
    }
}

private fun Instant.format() = toLocalDateTime(TimeZone.currentSystemDefault()).let {
    "${it.dayOfMonth} ${it.month.name.lowercase().capitalize(Locale.current)} ${it.year}"
}