package ksnd.open.hiraganaconverter.view.parts.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun CustomFilledTonalIconButton(
    modifier: Modifier = Modifier,
    contentDescription: String,
    painter: Painter,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    FilledTonalIconButton(
        modifier = modifier
            .size(48.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        onClick = onClick,
        interactionSource = buttonScaleState.interactionSource,
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewConvertButton_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CustomFilledTonalIconButton(
                contentDescription = "",
                painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConvertButton_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CustomFilledTonalIconButton(
                contentDescription = "",
                painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                onClick = {},
            )
        }
    }
}
