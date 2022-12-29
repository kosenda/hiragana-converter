package ksnd.open.hiraganaconverter.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import ksnd.open.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.open.hiraganaconverter.view.parts.button.CustomFontRadioButton
import ksnd.open.hiraganaconverter.view.parts.button.CustomThemeRadioButton
import ksnd.open.hiraganaconverter.view.parts.card.TitleCard
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.open.hiraganaconverter.viewmodel.PreviewSettingViewModel
import ksnd.open.hiraganaconverter.viewmodel.SettingsViewModel
import ksnd.open.hiraganaconverter.viewmodel.SettingsViewModelImpl

/**
 * 設定画面
 * ① テーマ設定
 * ② 言語設定
 * ③ フォント設定
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingDialog(
    onCloseClick: () -> Unit,
    settingsViewModel: SettingsViewModelImpl = hiltViewModel(),
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        SettingDialogContent(
            onCloseClick = onCloseClick,
            viewModel = settingsViewModel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        bottomBar = {
            BottomCloseButton(onClick = onCloseClick)
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // テーマ設定
            SettingThemeContent(
                onRadioButtonClick = viewModel::updateThemeNum,
                isSelectedNum = viewModel::isSelectedThemeNum,
            )
            // 言語設定
            SettingLanguageContent(
                onClick = {
                    isShowSelectLanguageDialog.value = true
                },
            )
            // フォント設定
            SettingFontContent(
                updateCustomFont = viewModel::updateCustomFont,
                isSelectedFont = viewModel::isSelectedFont,
                onCloseClick = onCloseClick,
            )
        }
    }
}

@Composable
private fun SettingThemeContent(
    onRadioButtonClick: (Int) -> Unit,
    isSelectedNum: (Int) -> Boolean,
) {
    val modeRadioResourcePairList: List<Triple<Int, String, Painter>> = listOf(
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
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(all = 8.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Column {
            modeRadioResourcePairList.forEach { resource ->
                CustomThemeRadioButton(
                    isSelected = isSelectedNum(resource.first),
                    buttonText = resource.second,
                    painter = resource.third,
                    onClick = { onRadioButtonClick(resource.first) },
                )
            }
        }
    }
}

@Composable
private fun SettingLanguageContent(onClick: () -> Unit) {
    val buttonScaleState = rememberButtonScaleState()
    TitleCard(
        text = stringResource(id = R.string.language_setting),
        painter = painterResource(id = R.drawable.ic_baseline_language_24),
    )
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .padding(all = 8.dp)
            .height(56.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .clickable(
                interactionSource = buttonScaleState.interactionSource,
                indication = null,
                onClick = onClick,
            ),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.select_language),
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SettingFontContent(
    updateCustomFont: (CustomFont) -> Unit,
    isSelectedFont: (CustomFont) -> Boolean,
    onCloseClick: () -> Unit,
) {
    TitleCard(
        text = stringResource(id = R.string.font_setting),
        painterResource(id = R.drawable.ic_baseline_text_fields_24),
    )
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(all = 8.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Column {
            CustomFontRadioButton(
                onClick = {
                    updateCustomFont(CustomFont.DEFAULT)
                    onCloseClick()
                },
                selected = isSelectedFont(CustomFont.DEFAULT),
                text = stringResource(id = R.string.default_font),
                fontFamily = FontFamily.Default,
            )
            CustomFontRadioButton(
                onClick = {
                    updateCustomFont(CustomFont.CORPORATE_LOGO_ROUNDED)
                    onCloseClick()
                },
                selected = isSelectedFont(CustomFont.CORPORATE_LOGO_ROUNDED),
                text = stringResource(id = R.string.corporate_logo_rounded_font),
                fontFamily = FontFamily(Font(R.font.corporate_logo_rounded_bold_ver3)),
            )
            CustomFontRadioButton(
                onClick = {
                    updateCustomFont(CustomFont.CORPORATE_YAWAMIN)
                    onCloseClick()
                },
                selected = isSelectedFont(CustomFont.CORPORATE_YAWAMIN),
                text = stringResource(id = R.string.corporate_yawamin_font),
                fontFamily = FontFamily(Font(R.font.corporate_yawamin_ver3)),
            )
            CustomFontRadioButton(
                onClick = {
                    updateCustomFont(CustomFont.NOSUTARU_DOT_M_PLUS)
                    onCloseClick()
                },
                selected = isSelectedFont(CustomFont.NOSUTARU_DOT_M_PLUS),
                text = stringResource(id = R.string.nosutaru_dot_font),
                fontFamily = FontFamily(Font(R.font.nosutaru_dotmplush_10_regular)),
            )
            CustomFontRadioButton(
                onClick = {
                    updateCustomFont(CustomFont.BIZ_UDGOTHIC)
                    onCloseClick()
                },
                selected = isSelectedFont(CustomFont.BIZ_UDGOTHIC),
                text = stringResource(id = R.string.biz_udgothic),
                fontFamily = FontFamily(Font(R.font.bizudgothic_regular)),
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
