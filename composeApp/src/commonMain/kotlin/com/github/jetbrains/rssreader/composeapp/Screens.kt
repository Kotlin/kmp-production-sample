package com.github.jetbrains.rssreader.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.jetbrains.rssreader.app.FeedAction

object MainScreen {
    @Composable
    fun Content() {
        val store = AppStore
        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }
        MainFeed(
            store = store,
            onPostClick = { post ->
                post.link?.let { url ->
//                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            },
            onEditClick = {
//                navigator.push(FeedListScreen())
            }
        )
    }
}

//object FeedListScreen {
//    @Composable
//    fun Content() {
//        val store: FeedStore by inject()
//        FeedList(store = store)
//    }
//}