package com.github.jetbrains.rssreader.androidApp.ui.feedlist

import android.net.Uri
import android.view.View
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.ItemFeedUrlBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

data class FeedUrlItem(
    val url: String
) : AbstractItem<FeedUrlItem.ViewHolder>() {
    override val layoutRes = R.layout.item_feed_url
    override val type = R.id.fa_type_feed_url
    override fun getViewHolder(v: View) = ViewHolder(ItemFeedUrlBinding.bind(v))

    class ViewHolder(
        private val vb: ItemFeedUrlBinding
    ) : FastAdapter.ViewHolder<FeedUrlItem>(vb.root) {

        override fun bindView(item: FeedUrlItem, payloads: List<Any>) {
            vb.root.text = Uri.parse(item.url).host
        }

        override fun unbindView(item: FeedUrlItem) {}
    }
}