package com.github.jetbrains.rssreader.androidApp.ui.mainfeed

import android.view.View
import androidx.core.view.isVisible
import coil.load
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.databinding.ItemErrorBinding
import com.github.jetbrains.rssreader.androidApp.databinding.ItemPostBinding
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

data class ErrorItem(
    val error: Throwable
) : AbstractItem<ErrorItem.ViewHolder>() {
    override val layoutRes = R.layout.item_error
    override val type = R.id.fa_type_error
    override fun getViewHolder(v: View) = ViewHolder(ItemErrorBinding.bind(v))

    class ViewHolder(
        private val vb: ItemErrorBinding
    ) : FastAdapter.ViewHolder<ErrorItem>(vb.root) {
        override fun bindView(item: ErrorItem, payloads: List<Any>) {
            vb.root.text = item.error.toString()
        }

        override fun unbindView(item: ErrorItem) {}
    }
}