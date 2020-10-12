package com.github.jetbrains.rssreader.androidApp.ui.feedlist

import android.view.View
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.ItemFeedBinding
import com.github.jetbrains.rssreader.entity.Feed
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import java.util.*

data class FeedUrlItem(
    val feed: Feed
) : AbstractItem<FeedUrlItem.ViewHolder>() {
    override val layoutRes = R.layout.item_feed
    override val type = R.id.fa_type_feed
    override fun getViewHolder(v: View) = ViewHolder(ItemFeedBinding.bind(v))

    class ViewHolder(
        private val vb: ItemFeedBinding
    ) : FastAdapter.ViewHolder<FeedUrlItem>(vb.root) {

        override fun bindView(item: FeedUrlItem, payloads: List<Any>) {
            vb.titleTextView.text = item.feed.title
            vb.descriptionTextView.text = item.feed.description
            vb.shortTextView.text = item.feed.title
                .replace(" ", "")
                .take(2)
                .toUpperCase(Locale.getDefault())

            vb.shortTextView.isVisible = item.feed.imageUrl == null
            vb.imageView.load(item.feed.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_round)
                transformations(CircleCropTransformation())
            }
        }

        override fun unbindView(item: FeedUrlItem) {}
    }
}