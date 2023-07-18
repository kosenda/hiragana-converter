package ksnd.hiraganaconverter.view.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.CustomFont
import ksnd.hiraganaconverter.view.ThemeNum
import ksnd.hiraganaconverter.view.parts.button.CustomButton
import ksnd.hiraganaconverter.view.parts.button.CustomFontRadioButton
import ksnd.hiraganaconverter.view.parts.button.CustomThemeRadioButton
import ksnd.hiraganaconverter.view.parts.card.TitleCard
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.view.theme.fontFamily
import ksnd.hiraganaconverter.viewmodel.PreviewSettingViewModel
import ksnd.hiraganaconverter.viewmodel.SettingsViewModel
import ksnd.hiraganaconverter.viewmodel.SettingsViewModelImpl

@Composable
fun SettingDialog(
    onCloseClick: () -> Unit,
    settingsViewModel: SettingsViewModelImpl = hiltViewModel(),
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onCloseClick)
        SettingDialogContent(
            onCloseClick = onCloseClick,
            viewModel = settingsViewModel,
        )
    }
}

@Composable
private fun SettingDialogContent(
    onCloseClick: () -> Unit,
    viewModel: SettingsViewModel,
) {
    val isShowSelectLanguageDialog = rememberSaveable { mutableStateOf(false) }

    if (isShowSelectLanguageDialog.value) {
        SelectLanguageDialog(
            onCloseClick = { isShowSelectLanguageDialog.value = false },
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(all = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            DialogCloseButton(onCloseClick = onCloseClick)
            SettingThemeContent(
                onRadioButtonClick = viewModel::updateThemeNum,
                isSelectedNum = viewModel::isSelectedThemeNum,
            )
            SettingLanguageContent(
                onClick = {
                    isShowSelectLanguageDialog.value = true
                },
            )
            SettingFontContent(
                updateCustomFont = viewModel::updateCustomFont,
                isSelectedFont = viewModel::isSelectedFont,
            )
        }
    }
}

@Composable
private fun SettingThemeContent(
    onRadioButtonClick: (Int) -> Unit,
    isSelectedNum: (Int) -> Boolean,
) {
    val modeRadioResourceTriple: List<Triple<Int, String, Painter>> = listOf(
        Triple(
            ThemeNum.NIGHT.num,
            stringResource(id = R.string.dark_mode),
            painterResource(id = R.drawable.ic_baseline_brightness_2_24),
        ),
        Triple(
            ThemeNum.DAY.num,
            stringResource(id = R.string.light_mode),
            painterResource(id = R.drawable.ic_baseline_brightness_low_24),
        ),
        Triple(
            ThemeNum.AUTO.num,
            stringResource(id = R.string.auto_mode),
            painterResource(id = R.drawable.ic_baseline_brightness_auto_24),
        ),
    )

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
        modeRadioResourceTriple.map { resource ->
            val (themeNum, displayThemeName, painter) = resource
            CustomThemeRadioButton(
                isSelected = isSelectedNum(themeNum),
                buttonText = displayThemeName,
                painter = painter,
                onClick = { onRadioButtonClick(themeNum) },
            )
        }
    }
}

@Composable
private fun SettingLanguageContent(onClick: () -> Unit) {
    TitleCard(
        text = stringResource(id = R.string.language_setting),
        painter = painterResource(id = R.drawable.ic_baseline_language_24),
    )
    CustomButton(
        text = stringResource(id = R.string.select_language),
        onClick = onClick,
    )
}

@Composable
private fun SettingFontContent(
    updateCustomFont: (CustomFont) -> Unit,
    isSelectedFont: (CustomFont) -> Boolean,
) {
    val customFontResourcePair: List<Pair<CustomFont, String>> = listOf(
        Pair(
            CustomFont.DEFAULT,
            stringResource(id = R.string.default_font),
        ),
        Pair(
            CustomFont.CORPORATE_LOGO_ROUNDED,
            stringResource(id = R.string.corporate_logo_rounded_font),
        ),
        Pair(
            CustomFont.CORPORATE_YAWAMIN,
            stringResource(id = R.string.corporate_yawamin_font),
        ),
        Pair(
            CustomFont.NOSUTARU_DOT_M_PLUS,
            stringResource(id = R.string.nosutaru_dot_font),
        ),
        Pair(
            CustomFont.BIZ_UDGOTHIC,
            stringResource(id = R.string.biz_udgothic),
        ),
    )

    TitleCard(
        text = stringResource(id = R.string.font_setting),
        painterResource(id = R.drawable.ic_baseline_text_fields_24),
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        customFontResourcePair.map { resource ->
            val (customFont, displayFontName) = resource
            CustomFontRadioButton(
                onClick = { updateCustomFont(customFont) },
                selected = isSelectedFont(customFont),
                text = displayFontName,
                fontFamily = fontFamily(customFont.name),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingDialogContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SettingDialogContent(
                onCloseClick = {},
                viewModel = PreviewSettingViewModel(),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingDialogContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SettingDialogContent(
                onCloseClick = {},
                viewModel = PreviewSettingViewModel(),
            )
        }
    }
}
