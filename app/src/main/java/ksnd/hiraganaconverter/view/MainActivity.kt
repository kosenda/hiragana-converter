package ksnd.hiraganaconverter.view

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ShareCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.core.domain.inappreview.InAppReviewManager
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.view.navigation.Navigation
import ksnd.hiraganaconverter.viewmodel.MainActivityViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var analytics: Analytics

    @Inject lateinit var inAppReviewManager: InAppReviewManager

    private val mainViewModel: MainActivityViewModel by viewModels()
    private val updateFlowResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> mainViewModel.updateInAppUpdateState(state = InAppUpdateState.Downloading(0))
                RESULT_CANCELED -> mainViewModel.updateInAppUpdateState(state = InAppUpdateState.Canceled)
                else -> {
                    mainViewModel.updateInAppUpdateState(state = InAppUpdateState.Failed)
                    Timber.e("Failed in app update resultCode: ${result.resultCode}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentReader = ShareCompat.IntentReader(this, intent)
        val receivedText = if (intentReader.isShareIntent) intentReader.text else null

        var isAnimateSplash by mutableStateOf(true)
        CoroutineScope(Dispatchers.Default).launch {
            delay(800L)
            isAnimateSplash = false
        }
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isAnimateSplash }

        enableEdgeToEdge()

        setContent {
            val uiState by mainViewModel.uiState.collectAsState(initial = MainActivityUiState())
            val snackbarHostState = remember { SnackbarHostState() }

            val isDarkTheme =
                when (uiState.theme) {
                    Theme.NIGHT -> true
                    Theme.DAY -> false
                    else -> isSystemInDarkTheme()
                }

            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle =
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkTheme },
                    ),
                    navigationBarStyle =
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkTheme },
                    ),
                )
                onDispose { }
            }

            LaunchedEffect(uiState.inAppUpdateState) {
                when (uiState.inAppUpdateState) {
                    is InAppUpdateState.Requesting -> mainViewModel.requestInAppUpdate(activityResultLauncher = updateFlowResultLauncher)
                    is InAppUpdateState.Downloaded -> {
                        val snackbarResult =
                            snackbarHostState.showSnackbar(
                                message = this@MainActivity.getString(R.string.in_app_update_downloaded_snackbar_title),
                                actionLabel = this@MainActivity.getString(R.string.in_app_update_downloaded_action_label),
                                duration = SnackbarDuration.Indefinite,
                            )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            mainViewModel.startInAppUpdateInstall()
                        }
                    }
                    else -> Unit
                }
            }

            LaunchedEffect(uiState.needRequestReview) {
                if (uiState.needRequestReview) {
                    // TODO: Show a dialog to be created by the application instead of asking for a review out of the blue,
                    //  and call `inAppReviewManager.requestReview()`and`mainViewModel.completedRequestReview()` only when you can review it.
                    inAppReviewManager.requestReview()
                    mainViewModel.completedRequestReview()
                }
            }

            CompositionLocalProvider(LocalAnalytics provides analytics) {
                HiraganaConverterTheme(
                    isDarkTheme = isDarkTheme,
                    fontType = uiState.fontType,
                ) {
                    // Because the state changes before the animation ends
                    val downloadPercentage =
                        if (uiState.inAppUpdateState is InAppUpdateState.Downloading) {
                            (uiState.inAppUpdateState as InAppUpdateState.Downloading).percentage
                        } else {
                            100
                        }

                    Column {
                        InAppUpdateDownloadingCard(
                            text = this@MainActivity.getString(R.string.in_app_update_downloading_snackbar_title, downloadPercentage),
                            isVisible = uiState.inAppUpdateState is InAppUpdateState.Downloading,
                        )
                        Navigation(
                            modifier = Modifier.weight(1f),
                            snackbarHostState = snackbarHostState,
                            receivedText = receivedText,
                        )
                    }
                }
            }
        }
    }
}
