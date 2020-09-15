package com.github.jetbrains.rssreader.androidApp.ui

import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.logic.MainFeedState
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope
import timber.log.Timber

class MainFeedFragment : ReduxFragment<MainFeedState>(R.layout.fragment_main_feed) {
    private val scope: Scope by lazy { getKoin().getOrCreateScope<MainFeedFragment>(runId) }
    override val store by lazy { scope.get<MainFeed>() }

    override fun render(state: MainFeedState) {
        Timber.d(state.toString())
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        scope.close()
    }
}