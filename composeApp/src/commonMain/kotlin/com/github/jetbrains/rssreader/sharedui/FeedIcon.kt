package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.core.entity.Feed
import java.util.*

@Composable
fun FeedIcon(
    feed: Feed?,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val txtAll = _str("All")
    val shortName = remember(feed) { feed?.shortName() ?: txtAll }
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colors.secondary else Color.Transparent
            )
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .background(color = MaterialTheme.colors.primary)
                .clickable(enabled = onClick != null, onClick = onClick ?: {})
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onPrimary,
                text = shortName
            )
            feed?.imageUrl?.let { url ->
                AsyncImage(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private fun Feed.shortName(): String =
    title
        .replace(" ", "")
        .take(2)
        .uppercase(Locale.getDefault())

@Composable
fun EditIcon(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colors.secondary)
            .clickable(onClick = onClick)
    ) {
        LocalImage(
            modifier = Modifier.align(Alignment.Center),
            resName = "ic_edit.xml"
        )
    }
}