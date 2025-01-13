package ksnd.hiraganaconverter.core.ui.parts.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConversionTypeCard(
    onSelectedChange: (type: HiraKanaType) -> Unit,
) {
    var selectedTextType by rememberSaveable { mutableStateOf(HiraKanaType.HIRAGANA) }
    val buttonScaleState = rememberButtonScaleState()
    val haptics = LocalHapticFeedback.current

    val rotation by animateFloatAsState(
        targetValue = if (selectedTextType == HiraKanaType.HIRAGANA) 180f else 0f,
        animationSpec = tween(500),
        label = "",
    )

    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                    selectedTextType = when (selectedTextType) {
                        HiraKanaType.HIRAGANA -> HiraKanaType.KATAKANA
                        HiraKanaType.KATAKANA -> HiraKanaType.HIRAGANA
                    }
                    onSelectedChange(selectedTextType)
                },
            )
            .graphicsLayer {
                if (isTest().not()) rotationY = rotation
                cameraDistance = 10f * density
            },
        colors = CardDefaults.cardColors(
            containerColor = if (rotation <= 90f) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.tertiaryContainer
            },
        ),
    ) {
        if (rotation <= 90f || isTest()) {
            ConversionTypeSpinnerCardContent(
                selectedTextType = selectedTextType,
            )
        } else {
            ConversionTypeSpinnerCardContent(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                selectedTextType = selectedTextType,
            )
        }
    }
}

@Composable
private fun ConversionTypeSpinnerCardContent(
    selectedTextType: HiraKanaType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(all = 8.dp),
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

@UiModePreview
@Composable
fun PreviewConversionTypeCard() {
    HiraganaConverterTheme {
        ConversionTypeCard(onSelectedChange = {})
    }
}
