package ksnd.open.hiraganaconverter.view.parts.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ConversionTypeSpinnerCard(
    onSelectedChange: (type: HiraKanaType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTextType by rememberSaveable { mutableStateOf(HiraKanaType.HIRAGANA) }
    val buttonScaleState = rememberButtonScaleState()
    Row {
        OutlinedCard(
            modifier = Modifier
                .padding(all = 8.dp)
                .scale(scale = buttonScaleState.animationScale.value)
                .clickable(
                    interactionSource = buttonScaleState.interactionSource,
                    indication = null,
                    onClick = { expanded = true },
                ),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer),
        ) {
            ConversionTypeSpinnerCardContent(selectedTextType = selectedTextType)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 8.dp, y = 4.dp),
        ) {
            val conversionTypeArray = stringArrayResource(id = R.array.conversion_type)
            conversionTypeArray.forEach {
                DropdownMenuItem(
                    onClick = {
                        val selectHiraKanaType = when (it) {
                            conversionTypeArray[0] -> HiraKanaType.HIRAGANA
                            else -> HiraKanaType.KATAKANA
                        }
                        onSelectedChange(selectHiraKanaType)
                        selectedTextType = selectHiraKanaType
                        expanded = false
                    },
                    text = {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    },
                )
            }
        }
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
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Row {
            Text(
                text = when (selectedTextType) {
                    HiraKanaType.HIRAGANA -> stringArrayResource(id = R.array.conversion_type)[0]
                    HiraKanaType.KATAKANA -> stringArrayResource(id = R.array.conversion_type)[1]
                },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "spinner",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConversionTypeSpinnerCard_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        ConversionTypeSpinnerCard(onSelectedChange = {})
    }
}

@Preview
@Composable
private fun PreviewConversionTypeSpinnerCard_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        ConversionTypeSpinnerCard(onSelectedChange = {})
    }
}
