package ksnd.open.hiraganaconverter.view.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.parts.ConversionTypeSpinnerCard
import ksnd.open.hiraganaconverter.view.parts.TopBar
import ksnd.open.hiraganaconverter.view.parts.button.ConvertButton
import ksnd.open.hiraganaconverter.view.parts.button.CustomFilledTonalIconButton
import ksnd.open.hiraganaconverter.view.parts.button.MoveTopButton
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.viewmodel.ConvertViewModel
import ksnd.open.hiraganaconverter.viewmodel.ConvertViewModelImpl
import ksnd.open.hiraganaconverter.viewmodel.PreviewConvertViewModel

@Composable
fun ConverterScreen(convertViewModel: ConvertViewModelImpl = hiltViewModel()) {
    // ステータスバーとナビゲーションバーの色を設定する
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.surface

    SideEffect {
        systemUiController.setStatusBarColor(color)
        systemUiController.setNavigationBarColor(color)
    }

    // Preview用に切り離し
    ConverterScreenContent(
        viewModel = convertViewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConverterScreenContent(viewModel: ConvertViewModel) {
    val focusManager = LocalFocusManager.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val convertUiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar(scrollBehavior) },
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { MoveTopButton(scrollState = scrollState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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

            // エラーに何かある場合のみエラー表示を行う
            if (convertUiState.errorText != "") {
                ErrorCard(
                    errorText = convertUiState.errorText,
                    onClick = viewModel::clearErrorText,
                )
            }

            // 変換前用
            BeforeTextField(
                inputText = convertUiState.inputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = viewModel::updateInputText,
            )

            Divider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 50.dp),
            )

            // 変換後用
            AfterTextField(
                outputText = convertUiState.outputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = viewModel::updateOutputText,
            )

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun ErrorCard(
    errorText: String,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .clickable(
                interactionSource = buttonScaleState.interactionSource,
                indication = null,
                onClick = onClick,
            ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun BeforeTextField(
    inputText: String,
    clipboardManager: ClipboardManager,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    Row {
        Text(
            text = "[ ${stringResource(id = R.string.before_conversion)} ]",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )

        CustomFilledTonalIconButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            contentDescription = "copyText",
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
            onClick = {
                clipboardManager.setText(AnnotatedString(inputText))
                Toast.makeText(context, "COPIED.", Toast.LENGTH_SHORT).show()
            },
        )
    }
    OutlinedTextField(
        value = inputText,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        },
        textStyle = MaterialTheme.typography.titleMedium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.surface,
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.input_hint),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        modifier = Modifier
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth(),
    )
}

@Composable
private fun AfterTextField(
    outputText: String,
    clipboardManager: ClipboardManager,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    Row {
        Text(
            text = "[ ${stringResource(id = R.string.after_conversion)} ]",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        CustomFilledTonalIconButton(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                .size(48.dp),
            contentDescription = "copyText",
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
            onClick = {
                clipboardManager.setText(AnnotatedString(outputText))
                Toast.makeText(context, "COPIED.", Toast.LENGTH_SHORT).show()
            },
        )
    }
    OutlinedTextField(
        value = outputText,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions { focusManager.clearFocus() },
        textStyle = MaterialTheme.typography.titleMedium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.surface,
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.output_hint),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        },
        modifier = Modifier
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth(),
    )
}

@Preview
@Composable
private fun PreviewConverterScreenContent() {
    ConverterScreenContent(
        viewModel = PreviewConvertViewModel(),
    )
}
