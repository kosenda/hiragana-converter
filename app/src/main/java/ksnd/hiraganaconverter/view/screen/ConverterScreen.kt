package ksnd.hiraganaconverter.view.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.data.repository.LIMIT_CONVERT_COUNT
import ksnd.hiraganaconverter.model.ConvertErrorType
import ksnd.hiraganaconverter.view.TopBar
import ksnd.hiraganaconverter.viewmodel.ConvertViewModel
import ksnd.hiraganaconverter.viewmodel.ConvertViewModelImpl
import ksnd.hiraganaconverter.viewmodel.PreviewConvertViewModel

@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    convertViewModel: ConvertViewModelImpl,
) {
    ConverterScreenContent(
        modifier = modifier,
        viewModel = convertViewModel,
        snackbarHostState = snackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreenContent(
    modifier: Modifier = Modifier,
    viewModel: ConvertViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val focusManager = LocalFocusManager.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val convertUiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current.density
    var topBarHeight by remember { mutableIntStateOf(0) }
    val layoutDirection = LocalLayoutDirection.current

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = WindowInsets.displayCutout.asPaddingValues().calculateStartPadding(layoutDirection),
                end = WindowInsets.displayCutout.asPaddingValues().calculateEndPadding(layoutDirection),
            )
            .padding(
                start = WindowInsets.navigationBars.asPaddingValues().calculateStartPadding(layoutDirection),
                end = WindowInsets.navigationBars.asPaddingValues().calculateEndPadding(layoutDirection),
            ),
        topBar = {
            TopBar(
                modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { ksnd.hiraganaconverter.core.ui.parts.button.MoveTopButton(scrollState = scrollState) },
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
                    ksnd.hiraganaconverter.core.ui.parts.card.ConversionTypeCard(onSelectedChange = viewModel::changeHiraKanaType)
                }
                ksnd.hiraganaconverter.core.ui.parts.button.CustomButtonWithBackground(
                    id = R.drawable.ic_reset,
                    convertDescription = "reset",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = viewModel::clearAllText,
                )
                ksnd.hiraganaconverter.core.ui.parts.button.ConvertButton(
                    modifier = Modifier.padding(start = 4.dp),
                    id = R.drawable.ic_baseline_compare_arrows_24,
                    isConverting = convertUiState.isConverting,
                    convertDescription = stringResource(id = R.string.conversion),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = viewModel::convert,
                )
            }

            convertUiState.convertErrorType?.let {
                ksnd.hiraganaconverter.core.ui.parts.card.ErrorCard(
                    errorText = when (it) {
                        ConvertErrorType.TOO_MANY_CHARACTER -> stringResource(id = R.string.request_too_large)
                        ConvertErrorType.RATE_LIMIT_EXCEEDED -> stringResource(id = R.string.limit_exceeded)
                        ConvertErrorType.CONVERSION_FAILED -> stringResource(id = R.string.conversion_failed)
                        ConvertErrorType.INTERNAL_SERVER -> stringResource(id = R.string.internal_server_error)
                        ConvertErrorType.NETWORK -> stringResource(id = R.string.network_error)
                        ConvertErrorType.REACHED_CONVERT_MAX_LIMIT -> stringResource(
                            id = R.string.limit_local_count,
                            LIMIT_CONVERT_COUNT,
                        )
                    },
                    onClick = viewModel::clearConvertErrorType,
                )
            }

            BeforeOrAfterTextField(
                isBefore = true,
                text = convertUiState.inputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = viewModel::updateInputText,
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
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

        ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton(
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

@ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
@Composable
private fun PreviewConverterScreenContent() {
    ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        ConverterScreenContent(
            viewModel = PreviewConvertViewModel(),
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
