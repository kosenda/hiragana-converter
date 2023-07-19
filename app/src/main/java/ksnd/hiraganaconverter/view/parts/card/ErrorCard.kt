package ksnd.hiraganaconverter.view.parts.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.extension.noRippleClickable
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ErrorCard(
    errorText: String,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewErrorCard_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        ErrorCard(errorText = stringResource(id = R.string.limit_exceeded), onClick = {})
    }
}

@Preview
@Composable
private fun PreviewErrorCard_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        ErrorCard(errorText = stringResource(id = R.string.conversion_failed), onClick = {})
    }
}
