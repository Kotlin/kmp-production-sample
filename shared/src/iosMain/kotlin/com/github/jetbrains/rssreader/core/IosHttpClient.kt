package com.github.jetbrains.rssreader.core

import com.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.logging.*

internal fun IosHttpClient(withLog: Boolean) = HttpClient(Ios) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "IosHttpClient", message = message)
            }
        }
    }
}