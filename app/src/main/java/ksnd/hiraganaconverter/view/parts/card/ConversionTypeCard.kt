package ksnd.hiraganaconverter.view.parts.card

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.view.extension.noRippleClickable
import ksnd.hiraganaconverter.view.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ConversionTypeCard(
    onSelectedChange: (type: HiraKanaType) -> Unit,
) {
    var selectedTextType by rememberSaveable { mutableStateOf(HiraKanaType.HIRAGANA) }
    val buttonScaleState = rememberButtonScaleState()
    val localView = LocalView.current

    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = {
                    localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    selectedTextType = when (selectedTextType) {
                        HiraKanaType.HIRAGANA -> HiraKanaType.KATAKANA
                        HiraKanaType.KATAKANA -> HiraKanaType.HIRAGANA
                    }
                    onSelectedChange(selectedTextType)
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
    ) {
        ConversionTypeSpinnerCardContent(selectedTextType = selectedTextType)
    }
}

@Composable
private fun ConversionTypeSpinnerCardContent(selectedTextType: HiraKanaType) {
    Column(
        modifier = Modifier.padding(all = 8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.conversion_type),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Text(
            text = when (selectedTextType) {
                HiraKanaType.HIRAGANA -> stringArrayResource(id = R.array.conversion_type)[0]
                HiraKanaType.KATAKANA -> stringArrayResource(id = R.array.conversion_type)[1]
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewConversionTypeCard() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        ConversionTypeCard(onSelectedChange = {})
    }
}
