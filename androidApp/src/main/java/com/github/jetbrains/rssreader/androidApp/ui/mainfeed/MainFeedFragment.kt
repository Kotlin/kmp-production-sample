package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentMainFeedBinding
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.logic.MainFeedState
import com.github.jetbrains.rssreader.androidApp.ui.base.GenericDiffCallback
import com.github.jetbrains.rssreader.androidApp.ui.base.ReduxFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemPadding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
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
            addSystemPadding()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = FastAdapter.with(itemsAdapter).apply {
                onClickListener = { view, adapter, item, position ->
                    if (item is PostItem) {
                        item.post.link?.let {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                        }
                    }
                    false
                }
            }
        }
        vb.swipeRefreshLayout.setOnRefreshListener { store.onRefresh() }
    }

    override fun render(state: MainFeedState): Unit = when (state) {
        is MainFeedState.Empty -> {
            itemsAdapter.clear()
            vb.swipeRefreshLayout.isRefreshing = false
        }
        is MainFeedState.Progress -> {
            FastAdapterDiffUtil.set(
                itemsAdapter,
                state.posts.map { PostItem(it) },
                GenericDiffCallback
            )
            vb.swipeRefreshLayout.isRefreshing = true
        }
        is MainFeedState.Data -> {
            FastAdapterDiffUtil.set(
                itemsAdapter,
                state.posts.map { PostItem(it) },
                GenericDiffCallback
            )
            vb.swipeRefreshLayout.isRefreshing = false
        }
        is MainFeedState.Error -> {
            if (state.posts.isNotEmpty()) {
                FastAdapterDiffUtil.set(
                    itemsAdapter,
                    state.posts.map { PostItem(it) },
                    GenericDiffCallback
                )
                Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
            } else {
                itemsAdapter.clear()
                itemsAdapter.add(ErrorItem(state.error))
            }
            vb.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        scope.close()
    }
}