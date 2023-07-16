package ksnd.hiraganaconverter.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.hiraganaconverter.view.parts.card.LanguageCard
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.viewmodel.PreviewSelectLanguageViewModel
import ksnd.hiraganaconverter.viewmodel.SelectLanguageViewModel
import ksnd.hiraganaconverter.viewmodel.SelectLanguageViewModelImpl

@Composable
fun SelectLanguageDialog(
    onCloseClick: () -> Unit,
    selectLanguageViewModel: SelectLanguageViewModelImpl = hiltViewModel(),
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        SelectLanguageDialogContent(
            onCloseClick = onCloseClick,
            viewModel = selectLanguageViewModel,
        )
    }
}

@Composable
private fun SelectLanguageDialogContent(
    onCloseClick: () -> Unit,
    viewModel: SelectLanguageViewModel,
) {
    val displayLanguageList = stringArrayResource(id = R.array.display_language)

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(16.dp)),
        bottomBar = {
            BottomCloseButton(onClick = onCloseClick)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            displayLanguageList.mapIndexed { index, language ->
                LanguageCard(
                    modifier = Modifier.weight(1f),
                    onNewLanguageClick = viewModel::updateSelectLanguage,
                    index = index,
                    displayLanguage = language,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewSelectLanguageDialogContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectLanguageDialogContent(
                onCloseClick = {},
                PreviewSelectLanguageViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSelectLanguageDialogContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectLanguageDialogContent(
                onCloseClick = {},
                PreviewSelectLanguageViewModel(),
            )
        }
    }
}