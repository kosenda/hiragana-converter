package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButtonWithBackground(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    convertDescription: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    val haptics = LocalHapticFeedback.current

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        IconButton(
            modifier = modifier
                .padding(all = 8.dp)
                .size(size = 56.dp)
                .scale(scale = buttonScaleState.animationScale.value),
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                onClick()
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor),
            interactionSource = buttonScaleState.interactionSource,
        ) {
            Image(
                painter = painterResource(id = id),
                contentDescription = convertDescription,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(color = contentColor),
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewCustomButtonWithBackground() {
    HiraganaConverterTheme {
        CustomButtonWithBackground(
            id = R.drawable.ic_reset,
            convertDescription = stringResource(id = R.string.conversion),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = {},
        )
    }
}
