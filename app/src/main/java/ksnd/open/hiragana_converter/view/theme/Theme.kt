package ksnd.open.hiragana_converter.view.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightThemeColors = lightColorScheme()
private val DarkThemeColors = darkColorScheme()

/**
 * 本アプリのテーマ
 */
@Composable
fun HiraganaConverterTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    customFont: String,
    content: @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography(customFont = customFont),
        content = content
    )
}