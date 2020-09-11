package com.github.jetbrains.rssreader.datasource.network

import com.github.jetbrains.rssreader.entity.Feed
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

internal class FeedLoader(
    private val httpClient: HttpClient,
    private val parser: FeedParser
) {
    suspend fun getFeed(link: String): Feed {
        val xml = httpClient.get<HttpResponse>(link).readText()
        return parser.parse(xml)
    }
}