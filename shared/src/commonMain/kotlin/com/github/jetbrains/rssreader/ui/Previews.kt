@file:Suppress("DEPRECATION")

package com.github.jetbrains.rssreader.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jetbrains.rssreader.domain.Channel
import com.github.jetbrains.rssreader.domain.Image
import com.github.jetbrains.rssreader.domain.Item
import com.github.jetbrains.rssreader.domain.MediaContent
import com.github.jetbrains.rssreader.domain.RssFeed

@Preview
@Composable
private fun FeedItemPreview() {
    AppTheme {
        FeedItem(feed = PreviewData.feed) {}
    }
}

@Preview
@Composable
private fun PostPreview() {
    AppTheme {
        PostItem(item = PreviewData.post, onClick = {})
    }
}

@Preview
@Composable
private fun FeedIconPreview() {
    AppTheme {
        FeedIcon(feed = PreviewData.feed)
    }
}

@Preview
@Composable
private fun FeedIconSelectedPreview() {
    AppTheme {
        FeedIcon(feed = PreviewData.feed, true)
    }
}

private object PreviewData {
    val mediaContent = MediaContent(type = "image", url = "https://blog.jetbrains.com/wp-content/uploads/2020/11/server.png" )
    val post = Item(
        title = "Productive Server-Side Development With Kotlin: Stories From The Industry",
        description = "Kotlin was created as an alternative to Java, meaning that its application area within the JVM ecosystem was meant to be the same as Java’s. Obviously, this includes server-side development. We would love...",
        mediaContent = mediaContent,
        link = "https://blog.jetbrains.com/kotlin/2020/11/productive-server-side-development-with-kotlin-stories/",
        pubDate = "42",
        guid = "https://blog.jetbrains.com/?post_type=idea&#038;p=577488",
        contentEncoded = "Blah"
    )
    val image = Image(url = "https://blog.jetbrains.com/wp-content/uploads/2024/01/cropped-mstile-310x310-1-32x32.png", title = "The JetBrains Blog", link = "https://blog.jetbrains.com", width = 32, height = 32)
    val channel = Channel(
        title = "Kotlin Blog",
        link = "blog.jetbrains.com/kotlin/",
        description = "blog.jetbrains.com/kotlin/",
        copyright = "Copyright 2025 JetBrains",
        item = listOf(post),
        image = image
    )
    val feed = RssFeed(
        version = "2.0",
        channel = channel,
        sourceUrl = "https://blog.jetbrains.com/feed/",
        isDefault = false
    )
}
