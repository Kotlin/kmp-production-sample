package com.github.jetbrains.rssreader.androidApp

import android.content.Intent
import android.net.Uri
import com.github.jetbrains.rssreader.androidApp.ui.fragment.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.fragment.MainFeedFragment
import com.github.terrakok.modo.android.AppScreen
import com.github.terrakok.modo.android.ExternalScreen
import kotlinx.parcelize.Parcelize

@Suppress("FunctionName")
object Screens {
    @Parcelize
    class MainFeed : AppScreen("MainFeed") {
        override fun create() = MainFeedFragment()
    }

    @Parcelize
    class FeedList : AppScreen("FeedList") {
        override fun create() = FeedListFragment()
    }

    fun Browser(url: String) = ExternalScreen { Intent(Intent.ACTION_VIEW, Uri.parse(url)) }
}