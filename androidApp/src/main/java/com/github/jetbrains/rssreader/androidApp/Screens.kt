package com.github.jetbrains.rssreader.androidApp

import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun MainFeed() = FragmentScreen("MainFeed") { MainFeedFragment() }
    fun FeedList() = FragmentScreen("FeedList") { FeedListFragment() }
}