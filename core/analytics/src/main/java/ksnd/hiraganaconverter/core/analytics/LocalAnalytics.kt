package ksnd.hiraganaconverter.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * ref: https://github.com/android/nowinandroid/blob/15f8861da15ff75876af0f1383d23417eb79d89e/core/analytics/src/main/kotlin/com/google/samples/apps/nowinandroid/core/analytics/UiHelpers.kt
 */
val LocalAnalytics = staticCompositionLocalOf<Analytics> { MockAnalytics() }
