package com.github.jetbrains.rssreader.core.datasource.network

import com.github.jetbrains.rssreader.core.entity.Feed
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal class FeedLoader(
    private val httpClient: HttpClient,
    private val parser: FeedParser
) {
    suspend fun getFeed(url: String, isDefault: Boolean): Feed {
        val xml = httpClient.get(url).bodyAsText()
        return parser.parse(url, xml, isDefault)
    }
}