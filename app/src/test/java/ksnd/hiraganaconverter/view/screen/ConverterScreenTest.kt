package ksnd.hiraganaconverter.view.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import ksnd.hiraganaconverter.view.MainActivity
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.viewmodel.PreviewConvertViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel6Pro)
class ConverterScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun converterScreen_light() {
        captureRoboImage {
            HiraganaConverterTheme(isDarkTheme = false) {
                ConverterScreenContent(viewModel = PreviewConvertViewModel())
            }
        }
    }

    @Test
    fun converterScreen_dark() {
        captureRoboImage {
            HiraganaConverterTheme(isDarkTheme = true) {
                ConverterScreenContent(viewModel = PreviewConvertViewModel())
            }
        }
    }
}