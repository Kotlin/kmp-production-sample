package com.github.jetbrains.rssreader.androidApp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.github.jetbrains.app.FeedAction
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.Screens
import com.github.jetbrains.rssreader.androidApp.ui.compose.AppTheme
import com.github.jetbrains.rssreader.androidApp.ui.compose.MainFeedBottomBar
import com.github.jetbrains.rssreader.androidApp.ui.compose.PostList
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.launch
import com.github.terrakok.modo.back
import com.github.terrakok.modo.forward
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class MainFeedFragment : BaseFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val store: FeedStore by inject()
    private val modo: Modo by inject()
    private var effectJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (createMode != CreateMode.RESTORED_AFTER_ROTATION) {
            store.dispatch(FeedAction.Refresh(false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent { MainScreen(store, modo) }
    }

    override fun onResume() {
        super.onResume()
        effectJob = store.observeSideEffect().onEach { effect ->
            if (effect is FeedSideEffect.Error) {
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(this)
    }

    override fun onPause() {
        super.onPause()
        effectJob?.cancel()
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        modo.back()
    }
}

@Composable
private fun MainScreen(
    store: FeedStore,
    modo: Modo
) {
    AppTheme {
        ProvideWindowInsets {
            val state = store.observeState().collectAsState()
            val posts = remember(state.value.feeds, state.value.selectedFeed) {
                (state.value.selectedFeed?.posts ?: state.value.feeds.flatMap { it.posts })
                    .sortedByDescending { it.date }
            }
            Column {
                PostList(modifier = Modifier.weight(1f), posts = posts) { post ->
                    post.link?.let { modo.launch(Screens.Browser(it)) }
                }
                MainFeedBottomBar(
                    feeds = state.value.feeds,
                    selectedFeed = state.value.selectedFeed,
                    onFeedClick = { feed -> store.dispatch(FeedAction.SelectFeed(feed)) },
                    onEditClick = { modo.forward(Screens.FeedList()) }
                )
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}