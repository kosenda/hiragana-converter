package ksnd.hiraganaconverter.view

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ShareCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.AnalyticsHelper
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.core.domain.inappreview.InAppReviewManager
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.LocalIsConnectNetwork
import ksnd.hiraganaconverter.core.ui.LocalSharedTransitionScope
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.LocalIsDarkTheme
import ksnd.hiraganaconverter.view.navigation.Navigation
import ksnd.hiraganaconverter.viewmodel.MainActivityViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics: AnalyticsHelper

    @Inject
    lateinit var inAppReviewManager: InAppReviewManager

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

    @OptIn(ExperimentalSharedTransitionApi::class)
    @AddTrace(name = "MainActivity#onCreate")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                init {
                    mainViewModel.onNetworkConnectivityChanged(isConnectNetwork = connectivityManager.activeNetwork != null)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    mainViewModel.onNetworkConnectivityChanged(isConnectNetwork = true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    mainViewModel.onNetworkConnectivityChanged(isConnectNetwork = false)
                }
            },
        )

        val intentReader = ShareCompat.IntentReader(this, intent)
        val receivedText = if (intentReader.isShareIntent) intentReader.text else null

        setContent {
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            val isDarkTheme = when (uiState.theme) {
                Theme.NIGHT -> true
                Theme.DAY -> false
                else -> isSystemInDarkTheme()
            }

            val downloadPercentage = when (val inAppUpdateState = uiState.inAppUpdateState) {
                is InAppUpdateState.Downloading -> inAppUpdateState.percentage
                else -> 100
            }

            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkTheme },
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkTheme },
                    ),
                )
            }

            LaunchedEffect(uiState.inAppUpdateState) {
                when (uiState.inAppUpdateState) {
                    is InAppUpdateState.Requesting -> mainViewModel.requestInAppUpdate(activityResultLauncher = updateFlowResultLauncher)
                    is InAppUpdateState.Downloaded -> {
                        val snackbarResult = snackbarHostState.showSnackbar(
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

            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalAnalytics provides analytics,
                    LocalIsConnectNetwork provides uiState.isConnectNetwork,
                    LocalIsDarkTheme provides isDarkTheme,
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                ) {
                    HiraganaConverterTheme(
                        isDarkTheme = isDarkTheme,
                        fontType = uiState.fontType,
                    ) {
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

            if (uiState.isRequestingReview) {
                RequestReviewDialog(
                    onLater = mainViewModel::cancelledReview,
                    onOk = {
                        coroutineScope.launch { inAppReviewManager.requestReview() }
                        mainViewModel.completedRequestReview()
                    },
                )
            }
        }
    }
}
