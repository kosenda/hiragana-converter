package ksnd.hiraganaconverter.view

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.ui.LocalAnimatedVisibilityScope
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import ksnd.hiraganaconverter.feature.converter.ConverterScreen
import ksnd.hiraganaconverter.feature.history.ConvertHistoryScreen
import ksnd.hiraganaconverter.feature.history.detail.ConvertHistoryDetailScreen
import ksnd.hiraganaconverter.feature.info.InfoScreen
import ksnd.hiraganaconverter.feature.info.licence.LicenseScreen
import ksnd.hiraganaconverter.feature.info.licence.licensedetail.LicenseDetailScreen
import ksnd.hiraganaconverter.feature.setting.SettingScreen
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    receivedText: CharSequence?,
) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    fun navigateScreen(nav: Nav) {
        navController.navigate(nav) { launchSingleTop = true }
    }

    fun navigateHistoryDetail(historyData: ConvertHistoryData) {
        navController.navigate(Nav.HistoryDetail(historyData = historyData))
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
            CompositionLocalProvider(
                LocalAnimatedVisibilityScope provides this,
            ) {
                ConvertHistoryScreen(
                    viewModel = hiltViewModel(),
                    navigateHistoryDetail = ::navigateHistoryDetail,
                    onBackPressed = dropUnlessResumed(block = navController::navigateUp),
                )
            }
        }
        slideHorizontallyComposable<Nav.HistoryDetail>(
            typeMap = mapOf(typeOf<ConvertHistoryData>() to serializableType<ConvertHistoryData>()),
        ) {
            CompositionLocalProvider(
                LocalAnimatedVisibilityScope provides this,
            ) {
                ConvertHistoryDetailScreen(
                    history = it.toRoute<Nav.HistoryDetail>().historyData,
                    onBackPressed = dropUnlessResumed(block = navController::navigateUp),
                )
            }
        }
        slideHorizontallyComposable<Nav.Setting> {
            SettingScreen(
                viewModel = hiltViewModel(),
                onBackPressed = dropUnlessResumed(block = navController::navigateUp),
            )
        }
        slideHorizontallyComposable<Nav.Info> {
            InfoScreen(
                viewModel = hiltViewModel(),
                onBackPressed = dropUnlessResumed(block = navController::navigateUp),
                onClickLicense = dropUnlessResumed { navigateScreen(Nav.License) },
            )
        }
        slideHorizontallyComposable<Nav.License> {
            LicenseScreen(
                viewModel = hiltViewModel(),
                navigateLicenseDetail = ::navigateLicenseDetail,
                onBackPressed = dropUnlessResumed(block = navController::navigateUp),
            )
        }
        slideHorizontallyComposable<Nav.LicenseDetail> {
            LicenseDetailScreen(
                libraryName = it.toRoute<Nav.LicenseDetail>().libraryName,
                licenseContent = it.toRoute<Nav.LicenseDetail>().licenseContent,
                onBackPressed = dropUnlessResumed(block = navController::navigateUp),
            )
        }
    }
}

inline fun <reified T : Nav> NavGraphBuilder.slideHorizontallyComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    noinline content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        typeMap = typeMap,
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

// ref: https://medium.com/mercadona-tech/type-safety-in-navigation-compose-23c03e3d74a5
inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) = bundle.getString(key)?.let<String, T>(Json::decodeFromString)

    override fun parseValue(value: String): T = Json.decodeFromString(value)

    // Reasons to enclose in Uri〜: https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0-beta03:~:text=Bug%20Fixes-,Added,-documentation%20on%20NavType
    override fun serializeAsValue(value: T): String = Uri.encode(Json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putString(key, Json.encodeToString(value))
}
