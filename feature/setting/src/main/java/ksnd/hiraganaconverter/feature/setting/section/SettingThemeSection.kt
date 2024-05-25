package ksnd.hiraganaconverter.feature.setting.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
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
        ).forEachIndexed { index, (theme, displayThemeName, painter) ->
            CustomRadioButton(
                isSelected = theme == selectedTheme,
                buttonText = displayThemeName,
                painter = painter,
                onClick = { onRadioButtonClick(theme) },
            )
            if (index != FontType.entries.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.surface,
                )
            }
        }
    }
}

@Preview
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
