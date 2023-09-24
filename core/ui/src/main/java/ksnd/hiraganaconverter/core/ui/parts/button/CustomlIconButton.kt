package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.contentBrush
import ksnd.hiraganaconverter.core.ui.theme.secondaryBrush

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    contentDescription: String,
    painter: Painter,
    contentColor: Color? = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    IconButton(
        modifier = modifier
            .size(48.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        onClick = onClick,
        interactionSource = buttonScaleState.interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
        ),
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = if (contentColor == null) null else ColorFilter.tint(contentColor),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(32.dp)
                .then(
                    if (contentColor == null) {
                        Modifier
                    } else {
                        Modifier.contentBrush(brush = secondaryBrush())
                    },
                ),
        )
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewConvertButton() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CustomIconButton(
                contentDescription = "",
                painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                onClick = {},
            )
        }
    }
}
