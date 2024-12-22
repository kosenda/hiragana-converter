package ksnd.hiraganaconverter

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.LosslessWebPImageIoFormat
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowDisplay
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.screenshotid.AndroidPreviewScreenshotIdBuilder
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import kotlin.math.roundToInt

private const val SCREEN_SHOT_PATH = "../screenshots/"
private const val COMPARE_PATH = "../screenshots/compare/"

/**
 * ref:
 *   - https://github.com/takahirom/roborazzi
 *   - https://github.com/sergio-sastre/ComposablePreviewScanner
 *   - https://github.com/DeNA/android-modern-architecture-test-handson/blob/main/docs/handson/VisualRegressionTest_Preview_ComposablePreviewScanner.md
 */
@OptIn(ExperimentalRoborazziApi::class)
class RoborazziComposePreviewTest : ComposePreviewTester<AndroidPreviewInfo> {

    private val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun options(): ComposePreviewTester.Options {
        // Use composeTestRule as a JUnit 4 Rule
        val testLifecycleOptions = ComposePreviewTester.Options.JUnit4TestLifecycleOptions(
            testRuleFactory = { composeTestRule },
        )
        return super.options().copy(testLifecycleOptions = testLifecycleOptions)
    }

    override fun previews(): List<ComposablePreview<AndroidPreviewInfo>> = AndroidComposablePreviewScanner()
        // Configure roborazzi's packages in :app/build.gradle.kts
        .scanPackageTrees(*options().scanOptions.packages.toTypedArray())
        .getPreviews()

    override fun test(preview: ComposablePreview<AndroidPreviewInfo>) {
        val screenshotId = AndroidPreviewScreenshotIdBuilder(preview).build()
        val filePath = "$SCREEN_SHOT_PATH$screenshotId.webp"

        preview.apply {
            if (this.previewInfo.uiMode == Configuration.UI_MODE_NIGHT_YES) {
                RuntimeEnvironment.setQualifiers("+night")
            }

            setDisplaySize(this.previewInfo.widthDp, this.previewInfo.heightDp)
        }

        // Change the environment and regenerate the activity
        // Otherwise, environment will not be reflected
        composeTestRule.activityRule.scenario.recreate()

        composeTestRule.apply {
            setContent {
                preview()
            }
            onRoot().captureRoboImage(
                filePath = filePath,
                roborazziOptions = RoborazziOptions(
                    compareOptions = RoborazziOptions.CompareOptions(
                        outputDirectoryPath = COMPARE_PATH,
                    ),
                    recordOptions = RoborazziOptions.RecordOptions(
                        imageIoFormat = LosslessWebPImageIoFormat(),
                    ),
                ),
            )
        }
    }
}

private fun setDisplaySize(widthDp: Int, heightDp: Int) {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val display = ShadowDisplay.getDefaultDisplay()
    val density = context.resources.displayMetrics.density

    Shadows.shadowOf(display).apply {
        if (widthDp != -1) setWidth((widthDp * density).roundToInt())
        if (heightDp != -1) setHeight((heightDp * density).roundToInt())
    }
}
