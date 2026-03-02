package com.github.jetbrains.rssreader

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.ui.AppTheme
import com.github.jetbrains.rssreader.ui.FeedListScreen
import com.github.jetbrains.rssreader.ui.MainScreen
import com.github.jetbrains.rssreader.ui.RssFeedAppBar
import com.github.jetbrains.rssreader.ui.Screen
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssReaderApp(navController: NavHostController = rememberNavController()) {
    AppTheme {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()
        // Get the name of the current screen
        val currentScreen = Screen.valueOf(
            backStackEntry?.destination?.route ?: Screen.Main.name
        )
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                RssFeedAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier.padding(
                        WindowInsets.systemBars
                            .only(WindowInsetsSides.Bottom)
                            .asPaddingValues()
                    ), hostState = snackbarHostState
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Main.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = Screen.Main.name) {
                    MainScreen(
                        onEditClick = { navController.navigate(Screen.FeedList.name) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
                composable(route = Screen.FeedList.name) {
                    FeedListScreen()
                }
            }

            val store: FeedStore = koinInject<FeedStore>()
            val error = store.observeSideEffect()
                .filterIsInstance<FeedSideEffect.Error>()
                .collectAsState(null)
            LaunchedEffect(error.value) {
                error.value?.let {
                    snackbarHostState.showSnackbar(
                        it.error.message.toString()
                    )
                }
            }
        }
    }
}