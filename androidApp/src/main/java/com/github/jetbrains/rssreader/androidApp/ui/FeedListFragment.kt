package com.github.jetbrains.rssreader.androidApp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.jetbrains.app.FeedAction
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.ui.compose.AddFeedDialog
import com.github.jetbrains.rssreader.androidApp.ui.compose.AppTheme
import com.github.jetbrains.rssreader.androidApp.ui.compose.DeleteFeedDialog
import com.github.jetbrains.rssreader.androidApp.ui.compose.FeedItemList
import com.github.jetbrains.rssreader.entity.Feed
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class FeedListFragment : BaseFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val store: FeedStore by inject()
    private val modo: Modo by inject()
    private var effectJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            FeedListScreen(store)
        }
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
private fun FeedListScreen(
    store: FeedStore
) {
    AppTheme {
        ProvideWindowInsets {
            Box {
                val state = store.observeState().collectAsState()
                val showAddDialog = remember { mutableStateOf(false) }
                val feedForDelete = remember<MutableState<Feed?>> { mutableStateOf(null) }
                FeedItemList(feeds = state.value.feeds) {
                    feedForDelete.value = it
                }
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .navigationBarsWithImePadding(),
                    onClick = { showAddDialog.value = true }
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
                        contentDescription = null
                    )
                }
                if (showAddDialog.value) {
                    AddFeedDialog(
                        onAdd = {
                            store.dispatch(FeedAction.Add(it))
                            showAddDialog.value = false
                        },
                        onDismiss = {
                            showAddDialog.value = false
                        }
                    )
                }
                feedForDelete.value?.let { feed ->
                    DeleteFeedDialog(
                        feed = feed,
                        onDelete = {
                            store.dispatch(FeedAction.Delete(feed.sourceUrl))
                            feedForDelete.value = null
                        },
                        onDismiss = {
                            feedForDelete.value = null
                        }
                    )
                }
            }
        }
    }
}