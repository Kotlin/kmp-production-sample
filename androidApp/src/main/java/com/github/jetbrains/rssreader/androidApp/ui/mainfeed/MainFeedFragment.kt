package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentMainFeedBinding
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.logic.MainFeedState
import com.github.jetbrains.rssreader.androidApp.ui.base.ReduxFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope

class MainFeedFragment : ReduxFragment<MainFeedState>(R.layout.fragment_main_feed) {
    private val scope: Scope by lazy { getKoin().getOrCreateScope<MainFeedFragment>(runId) }
    override val store by lazy { scope.get<MainFeed>() }

    private val vb by viewBinding(FragmentMainFeedBinding::bind)
    private val itemsAdapter = GenericItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = FastAdapter.with(itemsAdapter)
        }
        vb.swipeRefreshLayout.setOnRefreshListener { store.onRefresh() }
    }

    override fun render(state: MainFeedState): Unit = when (state) {
        is MainFeedState.Empty -> {
            itemsAdapter.clear()
            vb.swipeRefreshLayout.isRefreshing = false
        }
        is MainFeedState.Progress -> {
            itemsAdapter.clear()
            vb.swipeRefreshLayout.isRefreshing = true
        }
        is MainFeedState.Data -> {
            itemsAdapter.clear()
            state.feed.posts.forEach { post ->
                itemsAdapter.add(
                    PostItem(state.feed.title, state.feed.link, post)
                )
            }
            vb.swipeRefreshLayout.isRefreshing = false
        }
        is MainFeedState.Error -> {
            itemsAdapter.clear()
            itemsAdapter.add(ErrorItem(state.error))
            vb.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        scope.close()
    }
}