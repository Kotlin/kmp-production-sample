package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.jetbrains.rssreader.core.entity.Feed
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import rssreader.composeapp.generated.resources.Res

@Composable
fun FeedIcon(
    feed: Feed?,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val txtAll = stringResource(Res.string.all)
    val shortName = remember(feed) { feed?.shortName() ?: txtAll }
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
            )
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable(enabled = onClick != null, onClick = onClick ?: {})
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimary,
                text = shortName
            )
            feed?.imageUrl?.let { url ->
                Image(
                    painter = rememberImagePainter(url),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null
                )
            }
        }
    }
}

private fun Feed.shortName(): String =
    title
        .replace(" ", "")
        .take(2)
        .uppercase()

@Composable
fun EditIcon(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.secondary)
            .clickable(onClick = onClick)
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.ic_edit),
            modifier = Modifier.align(Alignment.Center),
            contentDescription = null
        )
    }
}