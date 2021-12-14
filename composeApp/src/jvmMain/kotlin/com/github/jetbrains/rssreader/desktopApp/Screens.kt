package com.github.jetbrains.rssreader.desktopApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.sharedui.FeedList
import com.github.jetbrains.rssreader.sharedui.MainFeed
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URI


class MainScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val navigator = LocalNavigator.currentOrThrow
        MainFeed(
            store,
            onPostClick = { post ->
                post.link?.let { openInBrowser(URI.create(it)) }
            },
            onEditClick = { navigator.push(FeedListScreen()) }
        )
    }
}

private fun openInBrowser(uri: URI) {
    val osName = System.getProperty("os.name")?.lowercase().orEmpty()
    when {
        "mac" in osName -> Runtime.getRuntime().exec("open $uri")
        "nix" in osName || "nux" in osName -> Runtime.getRuntime().exec("xdg-open $uri")
        else -> throw RuntimeException("cannot open $uri")
    }
}

class FeedListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        FeedList(store = store)
    }
}