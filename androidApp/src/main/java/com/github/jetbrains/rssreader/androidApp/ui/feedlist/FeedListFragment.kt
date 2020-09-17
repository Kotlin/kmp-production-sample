package com.github.jetbrains.rssreader.androidApp.ui.feedlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentFeedListBinding
import com.github.jetbrains.rssreader.androidApp.logic.FeedList
import com.github.jetbrains.rssreader.androidApp.logic.FeedListState
import com.github.jetbrains.rssreader.androidApp.ui.base.GenericDiffCallback
import com.github.jetbrains.rssreader.androidApp.ui.base.ReduxFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemBottomPadding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope

class FeedListFragment : ReduxFragment<FeedListState>(R.layout.fragment_feed_list) {
    private val scope: Scope by lazy { getKoin().getOrCreateScope<FeedListFragment>(runId) }
    override val store by lazy { scope.get<FeedList>() }

    private val vb by viewBinding(FragmentFeedListBinding::bind)
    private val itemsAdapter = GenericItemAdapter()
    private val deleteItemCallback: (String) -> Unit = { url ->
        AlertDialog.Builder(requireContext())
            .setMessage(url)
            .setPositiveButton(getString(R.string.remove)) { d, i ->
                store.removeFeed(url)
                d.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { d, i -> d.dismiss() }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.recyclerView.apply {
            addSystemBottomPadding()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = FastAdapter.with(itemsAdapter)
        }
    }

    override fun render(state: FeedListState) {
        FastAdapterDiffUtil.set(
            itemsAdapter,
            state.urls.map { FeedUrlItem(it, deleteItemCallback) } + NewFeedUrlItem,
            GenericDiffCallback
        )
        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.beginTransaction()
                .replace(R.id.container, MainFeedFragment())
                .commit()
        }
    }
}