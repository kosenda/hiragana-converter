package ksnd.hiraganaconverter

import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.github.takahirom.roborazzi.DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// ref: https://github.com/DroidKaigi/conference-app-2023/pull/217
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel6)
class PreviewTest(
    private val param: Pair<ShowkaseBrowserComponent, Int>,
) {

    @Test
    fun previewScreenshot() {
        val (showkaseBrowserComponent, count) = param
        val componentName = showkaseBrowserComponent.componentName.replace(" ", "")
        val filePath = DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH + "/" + componentName + "_" + count + ".png"
        captureRoboImage(filePath) {
            val newConfiguration = Configuration().apply {
                this.uiMode = if (componentName.contains(other = "dark", ignoreCase = true)) {
                    Configuration.UI_MODE_NIGHT_YES
                } else {
                    Configuration.UI_MODE_NIGHT_NO
                }
            }
            CompositionLocalProvider(LocalConfiguration provides newConfiguration) {
                showkaseBrowserComponent.component()
            }
        }
    }

    companion object {
        @ParameterizedRobolectricTestRunner.Parameters
        @JvmStatic
        fun components(): Iterable<Array<Any?>> {
            val countMap = mutableMapOf<String, Int>()
            return Showkase.getMetadata().componentList.map { showkaseBrowserComponent ->
                val componentName = showkaseBrowserComponent.componentName
                val count = countMap.getOrDefault(key = componentName, defaultValue = 0)
                countMap[componentName] = count + 1
                arrayOf(showkaseBrowserComponent to count)
            }
        }
    }
}
