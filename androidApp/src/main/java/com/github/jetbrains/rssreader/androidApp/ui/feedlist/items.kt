package com.github.jetbrains.rssreader.androidApp.ui.feedlist

import android.net.Uri
import android.view.View
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.ItemFeedUrlBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

data class FeedUrlItem(
    val url: String,
    val delete: (String) -> Unit
) : AbstractItem<FeedUrlItem.ViewHolder>() {
    override val layoutRes = R.layout.item_feed_url
    override val type = R.id.fa_type_feed_url
    override fun getViewHolder(v: View) = ViewHolder(ItemFeedUrlBinding.bind(v))

    class ViewHolder(
        private val vb: ItemFeedUrlBinding
    ) : FastAdapter.ViewHolder<FeedUrlItem>(vb.root) {
        private lateinit var item: FeedUrlItem

        init {
            vb.root.setOnCloseIconClickListener { item.delete(item.url) }
        }

        override fun bindView(item: FeedUrlItem, payloads: List<Any>) {
            this.item = item
            vb.root.text = Uri.parse(item.url).host
        }

        override fun unbindView(item: FeedUrlItem) {}
    }
}

object NewFeedUrlItem : AbstractItem<NewFeedUrlItem.ViewHolder>() {
    override val layoutRes = R.layout.item_feed_url
    override val type = R.id.fa_type_new_feed_url
    override fun getViewHolder(v: View) = ViewHolder(ItemFeedUrlBinding.bind(v))

    class ViewHolder(
        private val vb: ItemFeedUrlBinding
    ) : FastAdapter.ViewHolder<NewFeedUrlItem>(vb.root) {
        override fun bindView(item: NewFeedUrlItem, payloads: List<Any>) {
            vb.root.text = vb.root.context.getString(R.string.new_feed_url)
            vb.root.isCloseIconVisible = false
        }

        override fun unbindView(item: NewFeedUrlItem) {}
    }
}