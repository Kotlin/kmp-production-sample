package com.github.jetbrains.rssreader.core

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.logging.*

internal fun jsHttpClient(withLog: Boolean) = HttpClient(Js) {
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "JsHttpClient", message = message)
            }
        }
    }
}
