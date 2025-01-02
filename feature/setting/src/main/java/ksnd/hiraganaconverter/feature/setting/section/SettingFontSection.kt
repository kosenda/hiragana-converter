package ksnd.hiraganaconverter.feature.setting.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.TitleWithIcon
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingFontSection(
    selectFontType: FontType,
    onClickFontType: (FontType) -> Unit,
) {
    TitleWithIcon(
        title = R.string.font_setting,
        icon = R.drawable.ic_baseline_text_fields_24,
        modifier = Modifier.padding(top = 28.dp, bottom = 4.dp),
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        FontType.entries.forEach { fontType ->
            CustomRadioButton(
                isSelected = fontType == selectFontType,
                buttonText = fontType.fontName,
                onClick = { onClickFontType(fontType) },
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewSettingFontSection() {
    HiraganaConverterTheme {
        Surface {
            Column {
                SettingFontSection(FontType.DEFAULT) { }
            }
        }
    }
}
