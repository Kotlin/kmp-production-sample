package com.github.jetbrains.rssreader.core.datasource.network

internal interface DateParser {
    fun parse(date: String): Long

    companion object {
        const val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z"
    }
}