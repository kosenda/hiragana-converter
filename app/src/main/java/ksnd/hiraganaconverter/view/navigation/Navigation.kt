package ksnd.hiraganaconverter.view.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import ksnd.hiraganaconverter.feature.converter.ConverterScreen
import ksnd.hiraganaconverter.feature.history.ConvertHistoryScreen
import ksnd.hiraganaconverter.feature.info.InfoScreen
import ksnd.hiraganaconverter.feature.info.licence.LicenseScreen
import ksnd.hiraganaconverter.feature.info.licence.licensedetail.LicenseDetailScreen
import ksnd.hiraganaconverter.feature.setting.SettingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    receivedText: CharSequence?,
) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Ignore click events when you've started navigating to another screen
    // https://stackoverflow.com/a/76386604/4339442
    fun navigateUp() {
        val currentState = lifecycleOwner.lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            navController.navigateUp()
        }
    }

    fun navigateScreen(nav: Nav) {
        navController.navigate(nav) { launchSingleTop = true }
    }

    fun navigateLicenseDetail(libraryName: String, licenseContent: String) {
        navController.navigate(Nav.LicenseDetail(libraryName, licenseContent))
    }

    NavHost(
        navController = navController,
        startDestination = Nav.Converter(receivedText = receivedText?.toString() ?: ""),
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        fadeComposable<Nav.Converter> {
            ConverterScreen(
                viewModel = hiltViewModel(),
                snackbarHostState = snackbarHostState,
                scrollBehavior = scrollBehavior,
                navigateScreen = ::navigateScreen,
            )
        }
        slideHorizontallyComposable<Nav.History> {
            ConvertHistoryScreen(
                viewModel = hiltViewModel(),
                onBackPressed = ::navigateUp,
            )
        }
        slideHorizontallyComposable<Nav.Setting> {
            SettingScreen(
                viewModel = hiltViewModel(),
                onBackPressed = ::navigateUp,
            )
        }
        slideHorizontallyComposable<Nav.Info> {
            InfoScreen(
                viewModel = hiltViewModel(),
                onBackPressed = ::navigateUp,
                onClickLicense = { navigateScreen(Nav.License) },
            )
        }
        slideHorizontallyComposable<Nav.License> {
            LicenseScreen(
                viewModel = hiltViewModel(),
                navigateLicenseDetail = ::navigateLicenseDetail,
                onBackPressed = ::navigateUp,
            )
        }
        slideHorizontallyComposable<Nav.LicenseDetail> {
            LicenseDetailScreen(
                libraryName = it.toRoute<Nav.LicenseDetail>().libraryName,
                licenseContent = it.toRoute<Nav.LicenseDetail>().licenseContent,
                onBackPressed = ::navigateUp,
            )
        }
    }
}

inline fun <reified T : Nav> NavGraphBuilder.slideHorizontallyComposable(
    noinline content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(durationMillis = 500, easing = EaseInOutQuart),
                initialOffsetX = { fullWidth -> fullWidth * 2 / 10 },
            ) + fadeIn(
                animationSpec = tween(durationMillis = 500, easing = EaseOutQuad),
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(durationMillis = 500, easing = EaseInOutQuart),
                targetOffsetX = { fullWidth -> fullWidth * 2 / 10 },
            ) + fadeOut(
                animationSpec = tween(durationMillis = 500, easing = EaseOutQuad),
            )
        },
        content = content,
    )
}

inline fun <reified T : Nav> NavGraphBuilder.fadeComposable(
    noinline content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = { fadeIn(tween(durationMillis = 400)) },
        exitTransition = { fadeOut(tween(durationMillis = 400)) },
        content = content,
    )
}
