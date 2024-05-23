package ksnd.hiraganaconverter.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.CustomRadioButton
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SettingScreen(
    viewModel: SettingsViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(SettingsUiState())
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.SETTING)
    }

    SettingScreenContent(
        uiState = uiState,
        updateTheme = viewModel::updateTheme,
        updateFontType = viewModel::updateFontType,
        updateUseInAppUpdate = viewModel::updateEnableInAppUpdate,
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
    val layoutDirection = LocalLayoutDirection.current
    val isShowSelectLanguageDialog = rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var topBarHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current.density
    val navigationHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    if (isShowSelectLanguageDialog.value) {
        SelectLanguageDialog(
            onCloseClick = { isShowSelectLanguageDialog.value = false },
        )
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.title_settings),
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .noRippleClickable {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                    .onSizeChanged { topBarHeight = it.height },
                onBackPressed = onBackPressed,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .consumeWindowInsets(innerPadding)
                .padding(
                    start = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateEndPadding(layoutDirection),
                )
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height((topBarHeight / density).toInt().dp))
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
            Spacer(modifier = Modifier.height(48.dp + navigationHeight))
        }
    }
}

@Composable
private fun SettingThemeContent(
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
        FontType.entries.forEachIndexed { index, fontType ->
            CustomRadioButton(
                isSelected = fontType == selectFontType,
                buttonText = fontType.fontName,
                onClick = { onClickFontType(fontType) },
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

@UiModePreview
@Composable
fun PreviewSettingScreenContent() {
    HiraganaConverterTheme {
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
