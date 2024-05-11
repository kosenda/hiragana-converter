package ksnd.hiraganaconverter.feature.converter.preview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import ksnd.hiraganaconverter.core.ui.LocalIsConnectNetwork
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.converter.ConvertUiState
import ksnd.hiraganaconverter.feature.converter.ConverterScreenContent

// ref: https://issuetracker.google.com/issues/300116360
class LoremIpsum20Words : LoremIpsum(20)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SamplePreviewLoremIpsum(
    @PreviewParameter(LoremIpsum20Words::class) text: String,
) {
    HiraganaConverterTheme {
        CompositionLocalProvider(LocalIsConnectNetwork provides true) {
            ConverterScreenContent(
                uiState = ConvertUiState(inputText = text),
                // [NOTE]: PreviewParameterを使わなくとも使用できる
                // uiState = ConvertUiState(inputText = LoremIpsum(100).values.first()),
                snackbarHostState = remember { SnackbarHostState() },
                topBar = { },
                topBarHeight = 0,
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                changeHiraKanaType = {},
                clearAllText = {},
                convert = {},
                updateInputText = {},
                updateOutputText = {},
                hideErrorCard = {},
            )
        }
    }
}
