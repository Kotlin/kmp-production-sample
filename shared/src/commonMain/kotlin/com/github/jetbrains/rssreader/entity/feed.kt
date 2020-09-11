package com.github.jetbrains.rssreader.entity

data class Feed(
    val channel: Channel,
    val posts: List<Post>
)

data class Channel(
    val title: String,
    val link: String,
    val description: String,
)

data class Post(
    val title: String,
    val link: String,
    val description: String,
    val imageUrl: String?,
    val date: String,
)