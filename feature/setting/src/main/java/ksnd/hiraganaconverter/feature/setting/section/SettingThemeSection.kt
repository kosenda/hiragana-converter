package ksnd.hiraganaconverter.feature.setting.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingThemeSection(
    selectedTheme: Theme,
    onRadioButtonClick: (Theme) -> Unit,
) {
    TitleCard(
        text = stringResource(id = R.string.theme_setting),
        painter = painterResource(id = R.drawable.ic_baseline_brightness_4_24),
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        listOf(
            Triple(
                Theme.NIGHT,
                stringResource(id = R.string.dark_mode),
                painterResource(id = R.drawable.ic_baseline_brightness_2_24),
            ),
            Triple(
                Theme.DAY,
                stringResource(id = R.string.light_mode),
                painterResource(id = R.drawable.ic_baseline_brightness_low_24),
            ),
            Triple(
                Theme.AUTO,
                stringResource(id = R.string.auto_mode),
                painterResource(id = R.drawable.ic_baseline_brightness_auto_24),
            ),
        ).forEach { (theme, displayThemeName, painter) ->
            CustomRadioButton(
                isSelected = theme == selectedTheme,
                buttonText = displayThemeName,
                painter = painter,
                onClick = { onRadioButtonClick(theme) },
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewSettingThemeSection() {
    HiraganaConverterTheme {
        Surface {
            Column {
                SettingThemeSection(
                    selectedTheme = Theme.NIGHT,
                    onRadioButtonClick = {},
                )
            }
        }
    }
}
