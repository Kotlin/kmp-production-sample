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
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedState
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.FragmentFeedListBinding
import com.github.jetbrains.rssreader.androidApp.databinding.LayoutNewFeedUrlBinding
import com.github.jetbrains.rssreader.androidApp.ui.base.AppFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.addSystemPadding
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import com.github.jetbrains.rssreader.androidApp.ui.util.dp
import com.github.terrakok.cicerone.Router
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import org.koin.android.ext.android.inject

class FeedListFragment : AppFragment<FeedState, FeedSideEffect>(R.layout.fragment_feed_list) {
    override val store: FeedStore by inject()
    private val router: Router by inject()

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
                    store.dispatch(FeedAction.Add(input.replace("http://", "https://")))
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
                                store.dispatch(FeedAction.Delete(url))
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

    override fun onBackPressed() {
        super.onBackPressed()
        router.exit()
    }
}