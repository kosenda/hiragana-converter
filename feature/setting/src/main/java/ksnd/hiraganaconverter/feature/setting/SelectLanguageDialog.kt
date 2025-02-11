package ksnd.hiraganaconverter.feature.setting

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.LocaleListCompat
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.card.LanguageCard
import ksnd.hiraganaconverter.core.ui.parts.dialog.DialogTopBar
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun SelectLanguageDialog(
    onCloseClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onCloseClick)
        SelectLanguageDialogContent(
            onCloseClick = onCloseClick,
        )
    }
}

@Composable
private fun SelectLanguageDialogContent(
    onCloseClick: () -> Unit,
) {
    val analytics = LocalAnalytics.current
    var settingLocale by rememberSaveable { mutableStateOf("") }
    val languagePair = listOf(
        stringResource(id = R.string.locale_en) to stringResource(id = R.string.display_en),
        stringResource(id = R.string.locale_ja) to stringResource(id = R.string.display_ja),
    )

    LaunchedEffect(Unit) {
        val locale = AppCompatDelegate.getApplicationLocales()[0]
        locale?.let { settingLocale = locale.toLanguageTag() }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        topBar = {
            DialogTopBar(onCloseClick = onCloseClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            languagePair.forEach { (locale, displayLanguage) ->
                LanguageCard(
                    modifier = Modifier.weight(1f),
                    displayLanguage = displayLanguage,
                    isSelected = settingLocale == locale,
                    onClick = {
                        analytics.logEvent(Analytics.UpdateLanguage(displayLanguage))
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
                    },
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@UiModePreview
@Composable
fun PreviewSelectLanguageDialogContent() {
    HiraganaConverterTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectLanguageDialogContent(
                onCloseClick = {},
            )
        }
    }
}
