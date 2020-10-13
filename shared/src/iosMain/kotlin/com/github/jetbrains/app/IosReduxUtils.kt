package com.github.jetbrains.app

import com.github.jetbrains.rssreader.wrap

fun FeedStore.watchState() = observeState().wrap()
fun FeedStore.watchSideEffect() = observeSideEffect().wrap()