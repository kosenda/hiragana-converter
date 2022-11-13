package ksnd.open.hiragana_converter.view.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiragana_converter.R
import ksnd.open.hiragana_converter.model.Type

/**
 * 更新画面で使用するひらがな or カタカナ を選択できるSpinner
 */
@Composable
fun ConversionTypeSpinnerCard(
    selectedTextType: MutableState<Type>
) {

    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .padding(all = 8.dp)
            .clickable { expanded = true },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.conversion_type),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row {
                Text(
                    text = when(selectedTextType.value) {
                        Type.HIRAGANA -> stringArrayResource(id = R.array.conversion_type)[0]
                        Type.KATAKANA -> stringArrayResource(id = R.array.conversion_type)[1] },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "spinner",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    val conversionTypeArray = stringArrayResource(id = R.array.conversion_type)
                    conversionTypeArray.forEach {
                        DropdownMenuItem(
                            onClick = {
                                selectedTextType.value = when(it) {
                                    conversionTypeArray[0] -> Type.HIRAGANA
                                    else                   -> Type.KATAKANA
                                }
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConversionTypeSpinnerCard() {
    val selectedType = mutableStateOf(Type.HIRAGANA)
    ConversionTypeSpinnerCard(selectedTextType = selectedType)
}