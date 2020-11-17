package com.github.jetbrains.rssreader.androidApp

import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

@Suppress("FunctionName")
object Screens {
    fun MainFeed() = FragmentScreen { MainFeedFragment() }
    fun FeedList() = FragmentScreen { FeedListFragment() }
}