package ksnd.hiraganaconverter.view.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.converter.ConvertUiState
import ksnd.hiraganaconverter.feature.converter.ConverterScreenContent
import ksnd.hiraganaconverter.view.TopBar
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel6Pro)
class ConverterScreenTest {
    @Test
    fun converterScreen_light() {
        captureRoboImage {
            var topBarHeight by remember { mutableIntStateOf(0) }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            HiraganaConverterTheme(isDarkTheme = false) {
                ConverterScreenContent(
                    uiState = ConvertUiState(),
                    snackbarHostState = SnackbarHostState(),
                    topBar = {
                        TopBar(
                            modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                            scrollBehavior = scrollBehavior,
                            transitionHistory = {},
                            transitionSetting = {},
                            transitionInfo = {},
                        )
                    },
                    topBarHeight = topBarHeight,
                    scrollBehavior = scrollBehavior,
                    changeHiraKanaType = {},
                    clearAllText = {},
                    convert = {},
                    updateInputText = {},
                    updateOutputText = {},
                    hideErrorCard = {},
                )
            }
        }
    }

    @Test
    fun converterScreen_dark() {
        captureRoboImage {
            var topBarHeight by remember { mutableIntStateOf(0) }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            HiraganaConverterTheme(isDarkTheme = true) {
                ConverterScreenContent(
                    uiState = ConvertUiState(),
                    snackbarHostState = SnackbarHostState(),
                    topBar = {
                        TopBar(
                            modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                            scrollBehavior = scrollBehavior,
                            transitionHistory = {},
                            transitionSetting = {},
                            transitionInfo = {},
                        )
                    },
                    topBarHeight = topBarHeight,
                    scrollBehavior = scrollBehavior,
                    changeHiraKanaType = {},
                    clearAllText = {},
                    convert = {},
                    updateInputText = {},
                    updateOutputText = {},
                    hideErrorCard = {},
                )
            }
        }
    }
}
