package ksnd.hiraganaconverter.feature.setting

import android.content.res.Configuration
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.setting.section.SettingFontSection
import ksnd.hiraganaconverter.feature.setting.section.SettingInAppUpdateSection
import ksnd.hiraganaconverter.feature.setting.section.SettingLanguageSection
import ksnd.hiraganaconverter.feature.setting.section.SettingThemeSection

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
            SettingThemeSection(
                onRadioButtonClick = updateTheme,
                selectedTheme = uiState.theme,
            )
            SettingLanguageSection(
                onClick = {
                    isShowSelectLanguageDialog.value = true
                },
            )
            SettingFontSection(
                selectFontType = uiState.fontType,
                onClickFontType = { fontType -> updateFontType(fontType) },
            )
            SettingInAppUpdateSection(
                enableInAppUpdate = uiState.enableInAppUpdate,
                onCheckedChange = updateUseInAppUpdate,
            )
            Spacer(modifier = Modifier.height(48.dp + navigationHeight))
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, heightDp = 1300)
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, heightDp = 1300)
@Composable
fun PreviewSettingScreenContent() {
    HiraganaConverterTheme {
        Surface {
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
