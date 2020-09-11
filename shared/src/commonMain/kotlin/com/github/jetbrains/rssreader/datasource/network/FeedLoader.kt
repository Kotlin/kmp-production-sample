package com.github.jetbrains.rssreader.datasource.network

import com.github.jetbrains.rssreader.entity.Channel
import com.github.jetbrains.rssreader.entity.Feed
import com.github.jetbrains.rssreader.entity.Post

internal class FeedLoader {
    fun getFeed(link: String): Feed {
        return Feed(
            Channel("ch_title", link, "ch_description"),
            listOf(
                Post("p_title", "p_link", "p_description", null, "p_date"),
                Post("p_title", "p_link", "p_description", null, "p_date"),
                Post("p_title", "p_link", "p_description", null, "p_date"),
                Post("p_title", "p_link", "p_description", null, "p_date"),
                Post("p_title", "p_link", "p_description", null, "p_date"),
                Post("p_title", "p_link", "p_description", null, "p_date"),
            )
        )
    }
}