package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min)
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_chevron_right_24),
                contentDescription = "move screen",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp),
            )
        }
    }
}

@UiModeAndLocalePreview
@Composable
fun PreviewCustomButton() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CustomButton(text = stringResource(R.string.oss_licenses), onClick = {})
        }
    }
}