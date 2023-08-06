package ksnd.hiraganaconverter.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.view.screen.ConverterScreen
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var isAnimateSplash by mutableStateOf(true)
        CoroutineScope(Dispatchers.Default).launch {
            delay(800L)
            isAnimateSplash = false
        }
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isAnimateSplash }

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()

            val theme by mainViewModel.theme.collectAsState(initial = Theme.AUTO)
            val fontType by mainViewModel.fontType.collectAsState(initial = FontType.YUSEI_MAGIC)

            val isDarkTheme = when (theme) {
                Theme.NIGHT -> true
                Theme.DAY -> false
                else -> isSystemInDarkTheme()
            }

            CompositionLocalProvider(
                LocalIsDark provides isDarkTheme,
            ) {
                HiraganaConverterTheme(
                    isDarkTheme = isDarkTheme,
                    fontType = fontType,
                ) {
                    ConverterScreen()
                }
            }
        }
    }
}
