package ksnd.hiraganaconverter.view.parts.card

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ConversionTypeCard(
    onSelectedChange: (type: HiraKanaType) -> Unit,
) {
    var selectedTextType by rememberSaveable { mutableStateOf(HiraKanaType.HIRAGANA) }
    val buttonScaleState = rememberButtonScaleState()

    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .clickable(
                interactionSource = buttonScaleState.interactionSource,
                indication = null,
                onClick = {
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
            maxLines = 1,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

@Preview
@Composable
private fun PreviewConversionTypeSpinnerCard_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        ConversionTypeCard(onSelectedChange = {})
    }
}

@Preview
@Composable
private fun PreviewConversionTypeSpinnerCard_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        ConversionTypeCard(onSelectedChange = {})
    }
}
