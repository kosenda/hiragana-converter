package ksnd.hiraganaconverter.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

val LocalIsDarkTheme = staticCompositionLocalOf<Boolean> { false }

private val URL_DARK = Color(0xFF00F7FF)
private val URL_LIGHT = Color(0xFF0096A2)

object CustomColor {

    @Composable
    fun URL(): Color = if (LocalIsDarkTheme.current) URL_DARK else URL_LIGHT
}

@Composable
fun primaryBrush(): Brush = Brush.linearGradient(
    listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
    ),
)

@Composable
fun secondaryBrush(): Brush = Brush.linearGradient(
    listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
    ),
)

fun Modifier.contentBrush(brush: Brush) = this
    .graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
        }
    }
