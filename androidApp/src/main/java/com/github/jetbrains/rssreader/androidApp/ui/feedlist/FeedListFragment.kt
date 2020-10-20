package com.github.jetbrains.rssreader.androidApp.ui.feedlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedState
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentFeedListBinding
import com.github.jetbrains.rssreader.androidApp.databinding.LayoutNewFeedUrlBinding
import com.github.jetbrains.rssreader.androidApp.logic.FeedList
import com.github.jetbrains.rssreader.androidApp.ui.base.MvpFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemPadding
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import com.github.jetbrains.rssreader.androidApp.ui.util.dp
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope

class FeedListFragment : MvpFragment<FeedState, FeedSideEffect>(R.layout.fragment_feed_list) {
    private val scope: Scope by lazy { getKoin().getOrCreateScope<FeedListFragment>(runId) }
    override val presenter by lazy { scope.get<FeedList>() }

    private val vb by viewBinding(FragmentFeedListBinding::bind)
    private val itemsAdapter = GenericItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.addFab.doOnApplyWindowInsets { v, insets, initialPadding ->
            val lp = v.layoutParams as ViewGroup.MarginLayoutParams
            lp.updateMargins(bottom = 16.dp + insets.systemWindowInsetBottom)
            v.layoutParams = lp
            insets
        }
        vb.addFab.setOnClickListener {
            val dialogVB = LayoutNewFeedUrlBinding.inflate(LayoutInflater.from(context))
            AlertDialog.Builder(requireContext())
                .setView(dialogVB.root)
                .setPositiveButton(getString(R.string.add)) { d, i ->
                    val input = dialogVB.textInput.editText?.text.toString()
                    presenter.addFeed(input.replace("http://", "https://"))
                    d.dismiss()
                }
                .setNegativeButton(getString(R.string.cancel)) { d, i -> d.dismiss() }
                .show()
        }
        vb.recyclerView.apply {
            addSystemPadding()
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            itemAnimator = null
            adapter = FastAdapter.with(itemsAdapter).apply {
                onClickListener = { view, adapter, item, position ->
                    (item as? FeedUrlItem)?.feed?.sourceUrl?.let { url ->
                        AlertDialog.Builder(requireContext())
                            .setMessage(url)
                            .setPositiveButton(getString(R.string.remove)) { d, i ->
                                presenter.removeFeed(url)
                                d.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel)) { d, i -> d.dismiss() }
                            .show()
                    }
                    false
                }
            }
        }
    }

    override fun render(state: FeedState) {
        super.render(state)
        itemsAdapter.setNewList(
            state.feeds.map { FeedUrlItem(it) }
        )
    }

    override fun effect(effect: FeedSideEffect) {
        super.effect(effect)
        if (effect is FeedSideEffect.Error) {
            Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}