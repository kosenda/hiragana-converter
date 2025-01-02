package ksnd.hiraganaconverter.feature.setting.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.TitleWithIcon
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingLanguageSection(onClick: () -> Unit) {
    TitleWithIcon(
        title = R.string.language_setting,
        icon = R.drawable.ic_baseline_language_24,
        modifier = Modifier.padding(top = 28.dp, bottom = 4.dp),
    )
    TransitionButton(
        text = stringResource(id = R.string.select_language),
        onClick = onClick,
    )
}

@UiModePreview
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
