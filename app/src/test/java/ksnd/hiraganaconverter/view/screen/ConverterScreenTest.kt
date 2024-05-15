package ksnd.hiraganaconverter.view.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.converter.ConvertUiState
import ksnd.hiraganaconverter.feature.converter.ConverterScreenContent
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
            HiraganaConverterTheme(isDarkTheme = false) {
                ConverterScreenContent(
                    uiState = ConvertUiState(),
                    snackbarHostState = SnackbarHostState(),
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                    changeHiraKanaType = {},
                    clearAllText = {},
                    convert = {},
                    updateInputText = {},
                    updateOutputText = {},
                    hideErrorCard = {},
                    navigateScreen = {},
                )
            }
        }
    }

    @Test
    fun converterScreen_dark() {
        captureRoboImage {
            HiraganaConverterTheme(isDarkTheme = true) {
                ConverterScreenContent(
                    uiState = ConvertUiState(),
                    snackbarHostState = SnackbarHostState(),
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                    changeHiraKanaType = {},
                    clearAllText = {},
                    convert = {},
                    updateInputText = {},
                    updateOutputText = {},
                    hideErrorCard = {},
                    navigateScreen = {},
                )
            }
        }
    }
}
