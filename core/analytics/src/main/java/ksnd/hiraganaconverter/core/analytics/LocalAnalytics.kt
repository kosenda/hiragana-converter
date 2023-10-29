package ksnd.hiraganaconverter.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnalytics = staticCompositionLocalOf<Analytics> { MockAnalytics() }
