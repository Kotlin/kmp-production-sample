package com.github.jetbrains.rssreader.androidApp.ui.base

import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.diff.DiffCallback

object GenericDiffCallback : DiffCallback<GenericItem> {

    override fun areItemsTheSame(
        oldItem: GenericItem,
        newItem: GenericItem
    ) = oldItem.type == newItem.type

    override fun areContentsTheSame(
        oldItem: GenericItem,
        newItem: GenericItem
    ) = oldItem == newItem

    override fun getChangePayload(
        oldItem: GenericItem,
        oldItemPosition: Int,
        newItem: GenericItem,
        newItemPosition: Int
    ) = Any()
}