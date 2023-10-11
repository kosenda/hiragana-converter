package ksnd.hiraganaconverter.feature.setting

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingScreen(
    viewModel: SettingsViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState())

    SettingScreenContent(
        uiState = uiState,
        updateTheme = viewModel::updateTheme,
        updateFontType = viewModel::updateFontType,
        updateUseInAppUpdate = viewModel::updateUseInAppUpdate,
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreenContent(
    uiState: SettingsUiState,
    updateTheme: (Theme) -> Unit,
    updateFontType: (FontType) -> Unit,
    updateUseInAppUpdate: (Boolean) -> Unit,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isShowSelectLanguageDialog = rememberSaveable { mutableStateOf(false) }

    if (isShowSelectLanguageDialog.value) {
        SelectLanguageDialog(
            onCloseClick = { isShowSelectLanguageDialog.value = false },
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BackTopBar(scrollBehavior = scrollBehavior, onBackPressed = onBackPressed)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            SettingThemeContent(
                onRadioButtonClick = updateTheme,
                selectedTheme = uiState.theme,
            )
            SettingLanguageContent(
                onClick = {
                    isShowSelectLanguageDialog.value = true
                },
            )
            SettingFontContent(
                selectFontType = uiState.fontType,
                onClickFontType = { fontType -> updateFontType(fontType) },
            )
            SettingInAppUpdateContent(
                enableInAppUpdate = uiState.enableInAppUpdate,
                onCheckedChange = updateUseInAppUpdate,
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun SettingThemeContent(
    selectedTheme: Theme,
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
            val (theme, displayThemeName, painter) = resource
            CustomRadioButton(
                isSelected = theme == selectedTheme,
                buttonText = displayThemeName,
                painter = painter,
                onClick = { onRadioButtonClick(theme) },
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
    TransitionButton(
        text = stringResource(id = R.string.select_language),
        onClick = onClick,
    )
}

@Composable
private fun SettingFontContent(
    selectFontType: FontType,
    onClickFontType: (FontType) -> Unit,
) {
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
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            FontType.entries.forEach { fontType ->
                CustomRadioButton(
                    isSelected = fontType == selectFontType,
                    buttonText = fontType.fontName,
                    onClick = { onClickFontType(fontType) },
                )
            }
        }
    }
}

@UiModeAndLocalePreview
@Composable
fun PreviewSettingScreenContent() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SettingScreenContent(
                uiState = SettingsUiState(),
                updateTheme = {},
                updateFontType = {},
                updateUseInAppUpdate = {},
                onBackPressed = {},
            )
        }
    }
}
