package ksnd.hiraganaconverter.view.parts.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

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
    IconButton(
        modifier = modifier
            .padding(all = 8.dp)
            .size(size = 56.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        onClick = onClick,
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

@Preview
@Composable
private fun PreviewConvertButton_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        CustomButtonWithBackground(
            id = R.drawable.ic_baseline_compare_arrows_24,
            convertDescription = stringResource(id = R.string.conversion),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewConvertButton_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        CustomButtonWithBackground(
            id = R.drawable.ic_baseline_compare_arrows_24,
            convertDescription = stringResource(id = R.string.conversion),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = {},
        )
    }
}
