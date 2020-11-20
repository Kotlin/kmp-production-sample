package com.github.jetbrains.rssreader.androidApp

import android.content.Intent
import android.net.Uri
import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen

@Suppress("FunctionName")
object Screens {
    fun MainFeed() = FragmentScreen { MainFeedFragment() }
    fun FeedList() = FragmentScreen { FeedListFragment() }
    fun WebView(url: String) = ActivityScreen { Intent(Intent.ACTION_VIEW, Uri.parse(url)) }
}