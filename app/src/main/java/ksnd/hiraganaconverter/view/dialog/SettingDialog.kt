package ksnd.hiraganaconverter.view.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomButton
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.view.content.SettingInAppUpdateContent
import ksnd.hiraganaconverter.viewmodel.PreviewSettingViewModel
import ksnd.hiraganaconverter.viewmodel.SettingsViewModel
import ksnd.hiraganaconverter.viewmodel.SettingsViewModelImpl

@Composable
fun SettingDialog(
    settingsViewModel: SettingsViewModelImpl = hiltViewModel(),
    onCloseClick: () -> Unit,
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
    viewModel: SettingsViewModel,
    onCloseClick: () -> Unit,
) {
    val isShowSelectLanguageDialog = rememberSaveable { mutableStateOf(false) }
    val theme by viewModel.theme.collectAsState()
    val fontType by viewModel.fontType.collectAsState()
    val enableInAppUpdate by viewModel.enableInAppUpdate.collectAsState(true)

    if (isShowSelectLanguageDialog.value) {
        SelectLanguageDialog(
            onCloseClick = { isShowSelectLanguageDialog.value = false },
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DialogCloseButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                onCloseClick = onCloseClick,
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                SettingThemeContent(
                    onRadioButtonClick = viewModel::updateTheme,
                    isSelectedTheme = theme,
                )
                SettingLanguageContent(
                    onClick = {
                        isShowSelectLanguageDialog.value = true
                    },
                )
                SettingFontContent(
                    selectFontType = fontType,
                    onClickFontType = { fontType -> viewModel.updateFontType(newFontType = fontType) },
                )
                SettingInAppUpdateContent(
                    enableInAppUpdate = enableInAppUpdate,
                    onCheckedChange = viewModel::updateUseInAppUpdate,
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun SettingThemeContent(
    isSelectedTheme: Theme,
    onRadioButtonClick: (Theme) -> Unit,
) {
    val modeRadioResourceTriple: List<Triple<Theme, String, Painter>> = listOf(
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
    )

    ksnd.hiraganaconverter.core.ui.parts.card.TitleCard(
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
            val (theme, displayThemeName, painter) = resource
            ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton(
                isSelected = theme == isSelectedTheme,
                buttonText = displayThemeName,
                painter = painter,
                onClick = { onRadioButtonClick(theme) },
            )
        }
    }
}

@Composable
private fun SettingLanguageContent(onClick: () -> Unit) {
    ksnd.hiraganaconverter.core.ui.parts.card.TitleCard(
        text = stringResource(id = R.string.language_setting),
        painter = painterResource(id = R.drawable.ic_baseline_language_24),
    )
    ksnd.hiraganaconverter.core.ui.parts.button.CustomButton(
        text = stringResource(id = R.string.select_language),
        onClick = onClick,
    )
}

@Composable
private fun SettingFontContent(
    selectFontType: FontType,
    onClickFontType: (FontType) -> Unit,
) {
    ksnd.hiraganaconverter.core.ui.parts.card.TitleCard(
        text = stringResource(id = R.string.font_setting),
        painterResource(id = R.drawable.ic_baseline_text_fields_24),
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            FontType.entries.forEach { fontType ->
                ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton(
                    isSelected = fontType == selectFontType,
                    buttonText = fontType.fontName,
                    onClick = { onClickFontType(fontType) },
                )
            }
        }
    }
}

@ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
@Composable
fun PreviewSettingDialogContent() {
    ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
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
