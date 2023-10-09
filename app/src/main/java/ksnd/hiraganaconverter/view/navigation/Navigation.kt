package ksnd.hiraganaconverter.view.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ksnd.hiraganaconverter.core.domain.NavKey
import ksnd.hiraganaconverter.feature.converter.ConverterScreen
import ksnd.hiraganaconverter.feature.history.ConvertHistoryScreen
import ksnd.hiraganaconverter.feature.setting.SettingScreen
import ksnd.hiraganaconverter.view.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    receivedText: CharSequence?,
) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    var topBarHeight by remember { mutableIntStateOf(0) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Ignore click events when you've started navigating to another screen
    // https://stackoverflow.com/a/76386604/4339442
    fun navigateUp() {
        val currentState = lifecycleOwner.lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            navController.navigateUp()
        }
    }

    fun NavGraphBuilder.fadeComposable(
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
    ) {
        composable(
            route = route,
            arguments = arguments,
            enterTransition = { fadeIn(tween(durationMillis = 400)) },
            exitTransition = { fadeOut(tween(durationMillis = 400)) },
            content = content,
        )
    }

    NavHost(
        navController = navController,
        startDestination = "%s/{%s}".format(NavRoute.Converter.route, NavKey.RECEIVED_TEXT),
        modifier = modifier,
    ) {
        fadeComposable(
            route = "%s/{%s}".format(NavRoute.Converter.route, NavKey.RECEIVED_TEXT),
            arguments = listOf(navArgument(NavKey.RECEIVED_TEXT) {
                type = NavType.StringType
                defaultValue = receivedText?.toString() ?: ""
            }),
        ) {
            ConverterScreen(
                snackbarHostState = snackbarHostState,
                viewModel = hiltViewModel(),
                topBar = {
                    TopBar(
                        modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                        scrollBehavior = scrollBehavior,
                        transitionHistory = { navController.navigate(NavRoute.History.route) },
                        transitionSetting = { navController.navigate(NavRoute.Setting.route) },
                    )
                },
                topBarHeight = topBarHeight,
                scrollBehavior = scrollBehavior,
            )
        }
        fadeComposable(route = NavRoute.History.route) {
            ConvertHistoryScreen(
                viewModel = hiltViewModel(),
                onBackPressed = ::navigateUp,
            )
        }
        fadeComposable(route = NavRoute.Setting.route) {
            SettingScreen(
                viewModel = hiltViewModel(),
                onBackPressed = ::navigateUp,
            )
        }
    }
}
