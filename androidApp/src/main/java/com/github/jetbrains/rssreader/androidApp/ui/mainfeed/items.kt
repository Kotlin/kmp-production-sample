package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.view.View
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.ItemEditBinding
import com.github.jetbrains.rssreader.androidApp.databinding.ItemFeedShortBinding
import com.github.jetbrains.rssreader.androidApp.databinding.ItemPostBinding
import com.github.jetbrains.rssreader.androidApp.ui.util.shortName
import com.github.jetbrains.rssreader.entity.Feed
import com.github.jetbrains.rssreader.entity.Post
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

data class PostItem(
    val post: Post
) : AbstractItem<PostItem.ViewHolder>() {
    override val layoutRes = R.layout.item_post
    override val type = R.id.fa_type_post
    override fun getViewHolder(v: View) = ViewHolder(ItemPostBinding.bind(v))

    class ViewHolder(
        private val vb: ItemPostBinding
    ) : FastAdapter.ViewHolder<PostItem>(vb.root) {
        override fun bindView(item: PostItem, payloads: List<Any>) {
            vb.titleTextView.text = item.post.title
            vb.dateTextView.text = dateFormatter.format(Date(item.post.date))

            vb.descriptionTextView.text = item.post.description
            vb.descriptionTextView.isVisible = item.post.description != null

            vb.imageView.load(item.post.imageUrl)
            vb.imageView.isVisible = item.post.imageUrl != null
        }

        override fun unbindView(item: PostItem) {}
    }
}

data class FeedIconItem(
    val feed: Feed?,
    val isSelectedFeed: Boolean
) : AbstractItem<FeedIconItem.ViewHolder>() {
    override val layoutRes = R.layout.item_feed_short
    override val type = R.id.fa_type_feed_short
    override fun getViewHolder(v: View) = ViewHolder(ItemFeedShortBinding.bind(v))

    class ViewHolder(
        private val vb: ItemFeedShortBinding
    ) : FastAdapter.ViewHolder<FeedIconItem>(vb.root) {
        override fun bindView(item: FeedIconItem, payloads: List<Any>) {
            vb.selectionImageView.isVisible = item.isSelectedFeed
            vb.shortTextView.text = item.feed?.shortName()
                ?: vb.root.resources.getString(R.string.main)
            vb.shortTextView.isVisible = item.feed?.imageUrl == null
            vb.imageView.load(item.feed?.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_round)
                transformations(CircleCropTransformation())
            }
        }

        override fun unbindView(item: FeedIconItem) {}
    }
}

class EditIconItem : AbstractItem<EditIconItem.ViewHolder>() {
    override val layoutRes = R.layout.item_edit
    override val type = R.id.fa_type_edit
    override fun getViewHolder(v: View) = ViewHolder(ItemEditBinding.bind(v))

    class ViewHolder(
        private val vb: ItemEditBinding
    ) : FastAdapter.ViewHolder<EditIconItem>(vb.root) {
        override fun bindView(item: EditIconItem, payloads: List<Any>) {}
        override fun unbindView(item: EditIconItem) {}
    }
}