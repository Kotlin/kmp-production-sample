package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedState
import com.github.jetbrains.rssreader.androidApp.AppActivity
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentMainFeedBinding
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.ui.base.GenericDiffCallback
import com.github.jetbrains.rssreader.androidApp.ui.base.MvpFragment
import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemPadding
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import com.github.jetbrains.rssreader.androidApp.ui.util.dp
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope

class MainFeedFragment : MvpFragment<FeedState, FeedSideEffect>(R.layout.fragment_main_feed) {
    private val scope: Scope by lazy { getKoin().getOrCreateScope<MainFeedFragment>(runId) }
    override val presenter by lazy { scope.get<MainFeed>() }

    private val vb by viewBinding(FragmentMainFeedBinding::bind)
    private val itemsAdapter = GenericItemAdapter()
    private val fastAdapter = FastAdapter.with(itemsAdapter).apply {
        onClickListener = { view, adapter, item, position ->
            if (item is PostItem) {
                item.post.link?.let {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                }
            }
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.editFab.setOnClickListener {
            (activity as? AppActivity)?.showFragment(FeedListFragment())
        }
        vb.editFab.doOnApplyWindowInsets { v, insets, _ ->
            val lp = v.layoutParams as ViewGroup.MarginLayoutParams
            lp.updateMargins(bottom = 16.dp + insets.systemWindowInsetBottom)
            v.layoutParams = lp
            insets
        }
        vb.recyclerView.apply {
            addSystemPadding()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = fastAdapter
        }
        vb.swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        vb.swipeRefreshLayout.doOnApplyWindowInsets { v, insets, _ ->
            (v as SwipeRefreshLayout).setProgressViewOffset(
                false,
                0,
                insets.systemWindowInsetTop + 16.dp
            )
            insets
        }
    }

    override fun render(state: FeedState) {
        super.render(state)
        vb.swipeRefreshLayout.post {
            vb.swipeRefreshLayout.isRefreshing = state.progress
        }
        FastAdapterDiffUtil.set(
            itemsAdapter,
            state.feeds.flatMap { it.posts }.sortedByDescending { it.date }.map { PostItem(it) },
            GenericDiffCallback,
            false
        )
    }

    override fun effect(effect: FeedSideEffect) {
        super.effect(effect)
        if (effect is FeedSideEffect.Error) {
            Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        scope.close()
    }
}