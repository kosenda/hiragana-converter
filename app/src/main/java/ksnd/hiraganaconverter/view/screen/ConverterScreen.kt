package ksnd.hiraganaconverter.view.screen

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.LocalIsDark
import ksnd.hiraganaconverter.view.parts.TopBar
import ksnd.hiraganaconverter.view.parts.button.ConvertButton
import ksnd.hiraganaconverter.view.parts.button.CustomIconButton
import ksnd.hiraganaconverter.view.parts.button.MoveTopButton
import ksnd.hiraganaconverter.view.parts.card.ConversionTypeSpinnerCard
import ksnd.hiraganaconverter.view.parts.card.ErrorCard
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.viewmodel.ConvertViewModel
import ksnd.hiraganaconverter.viewmodel.ConvertViewModelImpl
import ksnd.hiraganaconverter.viewmodel.PreviewConvertViewModel

@Composable
fun ConverterScreen(convertViewModel: ConvertViewModelImpl = hiltViewModel()) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = LocalIsDark.current

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = isDarkTheme.not())
    }

    ConverterScreenContent(
        viewModel = convertViewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ConverterScreenContent(viewModel: ConvertViewModel) {
    val focusManager = LocalFocusManager.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val convertUiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current.density
    var topBarHeight by remember { mutableIntStateOf(0) }

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { MoveTopButton(scrollState = scrollState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        },
                    )
                },
        ) {
            Spacer(modifier = Modifier.height((topBarHeight / density).toInt().dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    ConversionTypeSpinnerCard(
                        onSelectedChange = viewModel::changeHiraKanaType,
                    )
                }
                ConvertButton(
                    onClick = {
                        viewModel.convert(context = context)
                    },
                )
            }

            if (convertUiState.errorText != "") {
                ErrorCard(
                    errorText = convertUiState.errorText,
                    onClick = viewModel::clearErrorText,
                )
            }

            BeforeOrAfterTextField(
                isBefore = true,
                text = convertUiState.inputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = viewModel::updateInputText,
            )

            Divider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
            )

            BeforeOrAfterTextField(
                isBefore = false,
                text = convertUiState.outputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = viewModel::updateOutputText,
            )

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun BeforeOrAfterTextField(
    isBefore: Boolean,
    text: String,
    clipboardManager: ClipboardManager,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val textColor = if (isBefore) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary

    Row {
        Text(
            text = if (isBefore) {
                String.format("〈 %s 〉", stringResource(id = R.string.before_conversion))
            } else {
                String.format("〈 %s 〉", stringResource(id = R.string.after_conversion))
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )

        CustomIconButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            contentDescription = "copyText",
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
            onClick = {
                clipboardManager.setText(AnnotatedString(text))
                Toast.makeText(context, "COPIED.", Toast.LENGTH_SHORT).show()
            },
        )
    }
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        },
        textStyle = MaterialTheme.typography.titleMedium,
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(
                text = if (isBefore) {
                    stringResource(id = R.string.input_hint)
                } else {
                    stringResource(id = R.string.output_hint)
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (isBefore) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.tertiary
                },
            )
        },
        modifier = Modifier
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth(),
    )
}

@Preview
@Composable
private fun PreviewConverterScreenContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        ConverterScreenContent(
            viewModel = PreviewConvertViewModel(),
        )
    }
}

@Preview
@Composable
private fun PreviewConverterScreenContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        ConverterScreenContent(
            viewModel = PreviewConvertViewModel(),
        )
    }
}
