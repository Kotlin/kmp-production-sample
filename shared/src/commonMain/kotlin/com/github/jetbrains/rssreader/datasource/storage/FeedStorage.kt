package com.github.jetbrains.rssreader.datasource.storage

import com.github.jetbrains.rssreader.entity.Feed
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class FeedStorage(
    private val settings: Settings,
    private val json: Json
) {
    private companion object {
        private const val KEY_FEED_CACHE = "key_feed_cache"
    }

    private var diskCache: Map<String, Feed>
        get() {
            return settings.getStringOrNull(KEY_FEED_CACHE)?.let { str ->
                json.decodeFromString(ListSerializer(FeedCacheItem.serializer()), str)
                    .associate { it.url to it.feed }
            } ?: mutableMapOf()
        }
        set(value) {
            val list = value.map { FeedCacheItem(it.key, it.value) }
            settings[KEY_FEED_CACHE] =
                json.encodeToString(ListSerializer(FeedCacheItem.serializer()), list)
        }

    private val memCache: MutableMap<String, Feed> by lazy { diskCache.toMutableMap() }

    suspend fun getFeed(url: String): Feed? = memCache[url]

    suspend fun saveFeed(url: String, feed: Feed) {
        memCache[url] = feed
        diskCache = memCache
    }

    suspend fun deleteFeed(url: String) {
        memCache.remove(url)
        diskCache = memCache
    }

    suspend fun getAllFeedUrls(): Set<String> = memCache.keys.toSet()
}

@Serializable
private data class FeedCacheItem(
    @SerialName("url") val url: String,
    @SerialName("feed") val feed: Feed
)