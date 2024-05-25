package ksnd.hiraganaconverter.feature.setting.section

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingLanguageSection(onClick: () -> Unit) {
    TitleCard(
        text = stringResource(id = R.string.language_setting),
        painter = painterResource(id = R.drawable.ic_baseline_language_24),
    )
    TransitionButton(
        text = stringResource(id = R.string.select_language),
        onClick = onClick,
    )
}

@Preview
@Composable
fun PreviewSettingLanguageSection() {
    HiraganaConverterTheme {
        Surface {
            Column {
                SettingLanguageSection {}
            }
        }
    }
}
