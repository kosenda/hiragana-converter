package ksnd.open.hiragana_converter.view.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.open.hiragana_converter.R
import ksnd.open.hiragana_converter.model.CustomFont
import ksnd.hiraganaconverter.view.parts.TitleCard
import ksnd.open.hiragana_converter.view.parts.BottomCloseButton
import ksnd.open.hiragana_converter.view.parts.CustomFontRadioButton
import ksnd.open.hiragana_converter.viewmodel.SettingsViewModel

/**
 * 設定画面
 * ① テーマ設定
 * ② 言語設定
 * ③ フォント設定
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingDialog(
    isShowDialog: MutableState<Boolean>,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {

    val isShowSelectLanguageDialog = rememberSaveable { mutableStateOf(false) }
    val modeRadio = listOf(
        stringResource(id = R.string.dark_mode),
        stringResource(id = R.string.light_mode),
        stringResource(id = R.string.auto_mode))

    // ラジオボタンの初期化
    LaunchedEffect(true) {
        settingsViewModel.getThemeNum()
        settingsViewModel.getCustomFont()
    }

    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        if(isShowSelectLanguageDialog.value) {
            SelectLanguageDialog(
                isShowDialog = isShowSelectLanguageDialog,
            )
        }
        Scaffold(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp)),
            bottomBar = {
                BottomCloseButton(onClick = { isShowDialog.value = false } )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                // テーマ設定
                TitleCard(text = stringResource(id = R.string.theme_setting))
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Column {
                        modeRadio.forEachIndexed{ index, buttonText ->
                            Row(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .clickable { settingsViewModel.updateThemeNum(index) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Image(
                                    painter = when (buttonText) {
                                        modeRadio[0] -> {
                                            painterResource(id = R.drawable.ic_baseline_brightness_2_24)
                                        }
                                        modeRadio[1] -> {
                                            painterResource(id = R.drawable.ic_baseline_brightness_low_24)
                                        }
                                        else -> {
                                            painterResource(id = R.drawable.ic_baseline_brightness_auto_24)
                                        }
                                    },
                                    contentDescription = buttonText,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = buttonText,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .weight(1f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                RadioButton(
                                    selected = settingsViewModel.isSelectedThemeNum(index),
                                    colors = RadioButtonDefaults.colors(),
                                    onClick = {
                                        settingsViewModel.updateThemeNum(index)
                                    }
                                )
                            }
                        }
                    }
                }

                // 言語設定
                TitleCard(text = stringResource(id = R.string.language_setting))
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .clickable(
                            onClick = { isShowSelectLanguageDialog.value = true }
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_language_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 16.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.select_language),
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                // フォント設定
                TitleCard(text = stringResource(id = R.string.font_setting))
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Column {
                        CustomFontRadioButton(
                            onClick = {
                                settingsViewModel.updateCustomFont(
                                    newCustomFont = CustomFont.DEFAULT
                                )
                                isShowDialog.value = false
                            },
                            selected = settingsViewModel.isSelectedFont(
                                       CustomFont.DEFAULT),
                            text = stringResource(id = R.string.default_font),
                            fontFamily = FontFamily.Default
                        )
                        CustomFontRadioButton(
                            onClick = {
                                settingsViewModel.updateCustomFont(
                                    newCustomFont = CustomFont.CORPORATE_LOGO_ROUNDED
                                )
                                isShowDialog.value = false
                            },
                            selected = settingsViewModel.isSelectedFont(
                                    CustomFont.CORPORATE_LOGO_ROUNDED),
                            text = stringResource(id = R.string.corporate_logo_rounded_font),
                            fontFamily = FontFamily(Font(R.font.corporate_logo_rounded_bold_ver3))
                        )
                        CustomFontRadioButton(
                            onClick = {
                                settingsViewModel.updateCustomFont(
                                    newCustomFont = CustomFont.CORPORATE_YAWAMIN
                                )
                                isShowDialog.value = false
                            },
                            selected = settingsViewModel.isSelectedFont(
                                    CustomFont.CORPORATE_YAWAMIN),
                            text = stringResource(id = R.string.corporate_yawamin_font),
                            fontFamily = FontFamily(Font(R.font.corporate_yawamin_ver3))
                        )
                        CustomFontRadioButton(
                            onClick = {
                                settingsViewModel.updateCustomFont(
                                    newCustomFont = CustomFont.NOSUTARU_DOT_M_PLUS
                                )
                                isShowDialog.value = false
                            },
                            selected = settingsViewModel.isSelectedFont(
                                CustomFont.NOSUTARU_DOT_M_PLUS),
                            text = stringResource(id = R.string.nosutaru_dot_font),
                            fontFamily = FontFamily(Font(R.font.nosutaru_dotmplush_10_regular))
                        )
                        CustomFontRadioButton(
                            onClick = {
                                settingsViewModel.updateCustomFont(
                                    newCustomFont = CustomFont.BIZ_UDGOTHIC
                                )
                                isShowDialog.value = false
                            },
                            selected = settingsViewModel.isSelectedFont(
                                    CustomFont.BIZ_UDGOTHIC),
                            text = stringResource(id = R.string.biz_udgothic),
                            fontFamily = FontFamily(Font(R.font.bizudgothic_regular))
                        )
                    }
                }
            }
        }
    }
}

