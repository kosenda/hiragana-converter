package ksnd.hiraganaconverter.view.parts.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    Button(
        modifier = modifier
            .padding(vertical = 8.dp)
            .defaultMinSize(minHeight = 48.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        onClick = onClick,
        interactionSource = buttonScaleState.interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.error,
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = stringResource(id = R.string.delete_all),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewDeleteButton() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DeleteButton(onClick = {})
        }
    }
}
