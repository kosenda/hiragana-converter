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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.feature.converter.ConverterScreen
import ksnd.hiraganaconverter.view.content.InAppUpdateDownloadingContent
import ksnd.hiraganaconverter.viewmodel.MainViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val updateFlowResultLauncher: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        var isAnimateSplash by mutableStateOf(true)
        CoroutineScope(Dispatchers.Default).launch {
            delay(800L)
            isAnimateSplash = false
        }
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isAnimateSplash }

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val theme by mainViewModel.theme.collectAsState(initial = Theme.AUTO)
            val fontType by mainViewModel.fontType.collectAsState(initial = FontType.YUSEI_MAGIC)
            val inAppUpdateState by mainViewModel.inAppUpdateState.collectAsState(initial = InAppUpdateState.Requesting)
            val snackbarHostState = remember { SnackbarHostState() }

            var topBarHeight by remember { mutableIntStateOf(0) }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

            val isDarkTheme = when (theme) {
                Theme.NIGHT -> true
                Theme.DAY -> false
                else -> isSystemInDarkTheme()
            }

            DisposableEffect(isDarkTheme) {
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
                onDispose { }
            }

            LaunchedEffect(inAppUpdateState) {
                when (inAppUpdateState) {
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

            HiraganaConverterTheme(
                isDarkTheme = isDarkTheme,
                fontType = fontType,
            ) {
                // Because the state changes before the animation ends
                val downloadPercentage = if (inAppUpdateState is InAppUpdateState.Downloading) {
                    (inAppUpdateState as InAppUpdateState.Downloading).percentage
                } else {
                    100
                }

                Column {
                    InAppUpdateDownloadingContent(
                        text = this@MainActivity.getString(R.string.in_app_update_downloading_snackbar_title, downloadPercentage),
                        isVisible = inAppUpdateState is InAppUpdateState.Downloading,
                    )
                    ConverterScreen(
                        modifier = Modifier.weight(1f),
                        snackbarHostState = snackbarHostState,
                        convertViewModel = hiltViewModel(),
                        topBar = {
                            TopBar(
                                modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                                scrollBehavior = scrollBehavior,
                            )
                        },
                        topBarHeight = topBarHeight,
                        scrollBehavior = scrollBehavior,
                    )
                }
            }
        }
    }
}
