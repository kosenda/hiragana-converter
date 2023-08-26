package ksnd.hiraganaconverter.view.parts.card

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.extension.noRippleClickable
import ksnd.hiraganaconverter.view.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun LanguageCard(
    modifier: Modifier = Modifier,
    displayLanguage: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    Card(
        modifier = modifier
            .padding(all = 16.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.secondaryContainer
            },
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = displayLanguage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        }
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewLanguageCard() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Column(Modifier.fillMaxWidth()) {
            LanguageCard(
                displayLanguage = stringResource(id = R.string.display_en),
                isSelected = true,
                onClick = {},
            )
        }
    }
}
