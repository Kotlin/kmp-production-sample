package com.github.jetbrains.rssreader.entity

data class Feed(
    val title: String,
    val link: String,
    val description: String,
    val imageUrl: String?,
    val posts: List<Post>
)

data class Post(
    val title: String?,
    val link: String?,
    val description: String?,
    val imageUrl: String?,
    val date: String
) {
    init {
        //by rss spec: https://www.rssboard.org/rss-specification#hrelementsOfLtitemgt
        require(title != null || link != null || description != null)
    }
}