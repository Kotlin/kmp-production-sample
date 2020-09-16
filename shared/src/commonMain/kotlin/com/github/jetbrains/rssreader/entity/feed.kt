package com.github.jetbrains.rssreader.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    @SerialName("title") val title: String,
    @SerialName("link") val link: String,
    @SerialName("description") val description: String,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("posts") val posts: List<Post>
)

@Serializable
data class Post(
    @SerialName("title") val title: String?,
    @SerialName("link") val link: String?,
    @SerialName("description") val description: String?,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("date") val date: String
) {
    init {
        //by rss spec: https://www.rssboard.org/rss-specification#hrelementsOfLtitemgt
        require(title != null || link != null || description != null)
    }
}