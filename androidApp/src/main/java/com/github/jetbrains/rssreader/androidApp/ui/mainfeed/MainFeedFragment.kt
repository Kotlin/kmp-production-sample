package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedState
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.app.mainFeedPosts
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.Screens
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentMainFeedBinding
import com.github.jetbrains.rssreader.androidApp.ui.base.AppFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemBottomPadding
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemPadding
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import com.github.jetbrains.rssreader.androidApp.ui.util.dp
import com.github.terrakok.cicerone.Router
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class MainFeedFragment : AppFragment<FeedState, FeedSideEffect>(R.layout.fragment_main_feed) {
    override val store: FeedStore by inject()
    private val router: Router by inject()

    private val vb by viewBinding(FragmentMainFeedBinding::bind)
    private val iconsAdapter = GenericItemAdapter()
    private val iconsFastAdapter = FastAdapter.with(iconsAdapter).apply {
        onClickListener = { view, adapter, item, position ->
            when (item) {
                is FeedIconItem -> {
                    vb.recyclerView.scrollToPosition(0)
                    store.dispatch(FeedAction.SelectFeed(item.feed))
                }
                is EditIconItem -> {
                    router.navigateTo(Screens.FeedList())
                }
            }
            false
        }
    }
    private val itemsAdapter = GenericItemAdapter()
    private val itemsFastAdapter = FastAdapter.with(itemsAdapter).apply {
        onClickListener = { view, adapter, item, position ->
            if (item is PostItem) {
                item.post.link?.let { router.navigateTo(Screens.WebView(it)) }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store.dispatch(FeedAction.Refresh(false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.recyclerView.apply {
            addSystemPadding()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            itemAnimator = null
            adapter = itemsFastAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var isShowed = true

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val showBottomPanel = dy <= 0
                    if (isShowed != showBottomPanel) {
                        isShowed = showBottomPanel
                        vb.feedsFrameLayout.animate().translationY(
                            if (showBottomPanel) 0F
                            else vb.feedsFrameLayout.height.toFloat()
                        )
                    }
                }
            })
        }
        vb.feedsFrameLayout.addSystemBottomPadding()
        vb.feedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            itemAnimator = null
            adapter = iconsFastAdapter
        }
        vb.swipeRefreshLayout.setOnRefreshListener { store.dispatch(FeedAction.Refresh(true)) }
        vb.swipeRefreshLayout.doOnApplyWindowInsets { v, insets, _ ->
            (v as SwipeRefreshLayout).setProgressViewOffset(
                false,
                0,
                insets.systemWindowInsetTop + 16.dp
            )
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        store.observeState().onEach {  }.launchIn(this)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun render(state: FeedState) {
        super.render(state)
        vb.swipeRefreshLayout.post {
            vb.swipeRefreshLayout.isRefreshing = state.progress
        }

        val feedIcons = buildList {
            add(FeedIconItem(null, state.selectedFeed == null))
            state.feeds.forEach { feed ->
                add(FeedIconItem(feed, feed == state.selectedFeed))
            }
            add(EditIconItem())
        }
        iconsAdapter.setNewList(feedIcons)
        itemsAdapter.setNewList(state.mainFeedPosts().map { PostItem(it) })
    }

    override fun effect(effect: FeedSideEffect) {
        super.effect(effect)
        if (effect is FeedSideEffect.Error) {
            Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        router.exit()
    }
}