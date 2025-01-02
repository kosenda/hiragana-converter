package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.contentBrush
import ksnd.hiraganaconverter.core.ui.theme.secondaryBrush

@Composable
fun CustomIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier.size(32.dp),
    contentColor: Color? = null,
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
            painter = painterResource(icon),
            contentDescription = contentDescription,
            colorFilter = contentColor?.let { ColorFilter.tint(it) },
            contentScale = ContentScale.Fit,
            modifier = iconModifier.then(
                if (contentColor == null) {
                    Modifier.contentBrush(brush = secondaryBrush())
                } else {
                    Modifier
                },
            ),
        )
    }
}

@UiModePreview
@Composable
fun PreviewCustomIconButton() {
    HiraganaConverterTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CustomIconButton(
                contentDescription = "",
                icon = R.drawable.ic_baseline_content_copy_24,
                onClick = {},
            )
        }
    }
}
