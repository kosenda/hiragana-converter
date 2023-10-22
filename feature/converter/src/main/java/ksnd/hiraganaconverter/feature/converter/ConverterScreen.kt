package ksnd.hiraganaconverter.feature.converter

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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.core.app.ShareCompat
import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.LIMIT_CONVERT_COUNT
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.ConvertButton
import ksnd.hiraganaconverter.core.ui.parts.button.CustomButtonWithBackground
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.card.ConversionTypeCard
import ksnd.hiraganaconverter.core.ui.parts.card.ErrorCard
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: ConvertViewModel,
    topBar: @Composable () -> Unit,
    topBarHeight: Int,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val uiState by viewModel.uiState.collectAsState(ConvertUiState())

    ConverterScreenContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        topBar = topBar,
        topBarHeight = topBarHeight,
        scrollBehavior = scrollBehavior,
        changeHiraKanaType = viewModel::changeHiraKanaType,
        clearAllText = viewModel::clearAllText,
        convert = viewModel::convert,
        clearConvertErrorType = viewModel::clearConvertErrorType,
        updateInputText = viewModel::updateInputText,
        updateOutputText = viewModel::updateOutputText,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreenContent(
    modifier: Modifier = Modifier,
    uiState: ConvertUiState,
    snackbarHostState: SnackbarHostState,
    topBar: @Composable () -> Unit,
    topBarHeight: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    changeHiraKanaType: (HiraKanaType) -> Unit,
    clearAllText: () -> Unit,
    convert: () -> Unit,
    clearConvertErrorType: () -> Unit,
    updateInputText: (String) -> Unit,
    updateOutputText: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val density = LocalDensity.current.density
    val layoutDirection = LocalLayoutDirection.current
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = WindowInsets.displayCutout
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.displayCutout
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            )
            .padding(
                start = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            ),
        topBar = topBar,
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
                    ConversionTypeCard(onSelectedChange = changeHiraKanaType)
                }
                CustomButtonWithBackground(
                    id = R.drawable.ic_reset,
                    convertDescription = "reset",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = clearAllText,
                )
                ConvertButton(
                    modifier = Modifier.padding(start = 4.dp),
                    id = R.drawable.ic_baseline_compare_arrows_24,
                    isConverting = uiState.isConverting,
                    convertDescription = stringResource(id = R.string.conversion),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = convert,
                )
            }

            uiState.convertErrorType?.let {
                ErrorCard(
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
                    onClick = clearConvertErrorType,
                )
            }

            BeforeOrAfterTextField(
                isBefore = true,
                text = uiState.inputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = updateInputText,
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
            )

            BeforeOrAfterTextField(
                isBefore = false,
                text = uiState.outputText,
                clipboardManager = clipboardManager,
                focusManager = focusManager,
                onValueChange = updateOutputText,
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

        if (isBefore.not()) {
            CustomIconButton(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                contentDescription = "share",
                painter = painterResource(id = R.drawable.baseline_share_24),
                onClick = {
                    context.startActivity(
                        ShareCompat.IntentBuilder(context)
                            .setType("text/plain")
                            .setText(text)
                            .createChooserIntent(),
                    )
                },
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@UiModeAndLocalePreview
@Composable
private fun PreviewConverterScreenContent() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        ConverterScreenContent(
            uiState = ConvertUiState(
                convertErrorType = ConvertErrorType.CONVERSION_FAILED,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            topBar = { },
            topBarHeight = 0,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
            changeHiraKanaType = {},
            clearAllText = {},
            convert = {},
            clearConvertErrorType = {},
            updateInputText = {},
            updateOutputText = {},
        )
    }
}
