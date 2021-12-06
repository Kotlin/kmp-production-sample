package com.github.jetbrains.rssreader.androidApp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.sharedui.FeedListScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FeedListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        FeedListScreen(store = store)
    }
}