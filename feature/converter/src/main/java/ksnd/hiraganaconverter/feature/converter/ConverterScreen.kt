package ksnd.hiraganaconverter.feature.converter

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.LIMIT_CONVERT_COUNT
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.LocalIsConnectNetwork
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import ksnd.hiraganaconverter.core.ui.parts.TopBar
import ksnd.hiraganaconverter.core.ui.parts.button.ConvertButton
import ksnd.hiraganaconverter.core.ui.parts.button.CustomButtonWithBackground
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.MoveTopButton
import ksnd.hiraganaconverter.core.ui.parts.card.ConversionTypeCard
import ksnd.hiraganaconverter.core.ui.parts.card.ERROR_CARD_ANIMATE_DURATION
import ksnd.hiraganaconverter.core.ui.parts.card.ErrorCard
import ksnd.hiraganaconverter.core.ui.parts.card.OfflineCard
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    viewModel: ConvertViewModel,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateScreen: (Nav) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ConvertUiState())
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.CONVERTER)
    }

    LaunchedEffect(uiState.showErrorCard) {
        if (uiState.convertErrorType != null && uiState.showErrorCard.not()) {
            // Prevents text from disappearing during Animation
            delay(ERROR_CARD_ANIMATE_DURATION.toLong())
            viewModel.clearConvertErrorType()
        }
    }

    ConverterScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        scrollBehavior = scrollBehavior,
        changeHiraKanaType = viewModel::changeHiraKanaType,
        clearAllText = viewModel::clearAllText,
        convert = viewModel::convert,
        updateInputText = viewModel::updateInputText,
        updateOutputText = viewModel::updateOutputText,
        hideErrorCard = viewModel::hideErrorCard,
        navigateScreen = navigateScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreenContent(
    uiState: ConvertUiState,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    changeHiraKanaType: (HiraKanaType) -> Unit,
    clearAllText: () -> Unit,
    convert: () -> Unit,
    updateInputText: (String) -> Unit,
    updateOutputText: (String) -> Unit,
    hideErrorCard: () -> Unit,
    navigateScreen: (Nav) -> Unit,
) {
    val density = LocalDensity.current.density
    val layoutDirection = LocalLayoutDirection.current
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()
    var topBarHeight by remember { mutableIntStateOf(0) }
    val navigationHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopBar(
                modifier = Modifier.onSizeChanged { topBarHeight = it.height },
                scrollBehavior = scrollBehavior,
                navigateScreen = navigateScreen,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { MoveTopButton(scrollState = scrollState) },
    ) { innerPadding ->
        ColumnScrollbar(
            state = scrollState,
            settings = ScrollbarSettings.Default.copy(
                thumbUnselectedColor = MaterialTheme.colorScheme.tertiaryContainer,
                thumbSelectedColor = MaterialTheme.colorScheme.tertiary,
                selectionMode = ScrollbarSelectionMode.Full,
            ),
        ) {
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(
                        start = WindowInsets.displayCutout
                            .asPaddingValues()
                            .calculateStartPadding(layoutDirection),
                        end = WindowInsets.displayCutout
                            .asPaddingValues()
                            .calculateEndPadding(layoutDirection),
                    )
                    .padding(horizontal = 8.dp)
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
                    ConversionTypeCard(onSelectedChange = changeHiraKanaType)
                    Spacer(modifier = Modifier.weight(1f))
                    CustomButtonWithBackground(
                        id = R.drawable.ic_reset,
                        convertDescription = "reset",
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = clearAllText,
                    )
                    ConvertButton(
                        modifier = Modifier.padding(start = 4.dp),
                        isConverting = uiState.isConverting,
                        onClick = convert,
                    )
                }

                OfflineCard()

                ErrorCard(
                    visible = uiState.showErrorCard,
                    errorText = when (uiState.convertErrorType) {
                        ConvertErrorType.TOO_MANY_CHARACTER -> stringResource(id = R.string.request_too_large)
                        ConvertErrorType.RATE_LIMIT_EXCEEDED -> stringResource(id = R.string.limit_exceeded)
                        ConvertErrorType.CONVERSION_FAILED -> stringResource(id = R.string.conversion_failed)
                        ConvertErrorType.INTERNAL_SERVER -> stringResource(id = R.string.internal_server_error)
                        ConvertErrorType.NETWORK -> stringResource(id = R.string.network_error)
                        ConvertErrorType.REACHED_CONVERT_MAX_LIMIT -> stringResource(
                            id = R.string.limit_local_count,
                            LIMIT_CONVERT_COUNT,
                        )

                        else -> ""
                    },
                    onClick = hideErrorCard,
                )

                BeforeAfterTextField(
                    isBefore = true,
                    text = uiState.inputText,
                    onValueChange = updateInputText,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )

                BeforeAfterTextField(
                    isBefore = false,
                    text = uiState.outputText,
                    onValueChange = updateOutputText,
                )

                Spacer(modifier = Modifier.height(120.dp + navigationHeight))
            }
        }
    }
}

@Composable
private fun BeforeAfterTextField(
    isBefore: Boolean,
    text: String,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current

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

        if (isBefore) {
            CustomIconButton(
                painter = painterResource(id = R.drawable.ic_baseline_content_paste_24),
                contentDescription = "",
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                onClick = {
                    onValueChange(clipboardManager.getText()?.text ?: "")
                },
            )
        } else {
            CustomIconButton(
                painter = painterResource(id = R.drawable.baseline_share_24),
                contentDescription = "",
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                onClick = {
                    context.startActivity(
                        ShareCompat.IntentBuilder(context)
                            .setType("text/plain")
                            .setText(text)
                            .createChooserIntent(),
                    )
                },
            )

            CustomIconButton(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                contentDescription = "",
                painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                onClick = {
                    clipboardManager.setText(AnnotatedString(text))
                    Toast.makeText(context, "COPIED.", Toast.LENGTH_SHORT).show()
                },
            )
        }
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
                text = stringResource(id = if (isBefore) R.string.input_hint else R.string.output_hint),
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
@UiModePreview
@Composable
fun PreviewConverterScreenContent(
    @PreviewParameter(PreviewConverterUiStateProvider::class) uiState: ConvertUiState,
) {
    HiraganaConverterTheme {
        CompositionLocalProvider(LocalIsConnectNetwork provides true) {
            ConverterScreenContent(
                uiState = uiState,
                snackbarHostState = remember { SnackbarHostState() },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                changeHiraKanaType = {},
                clearAllText = {},
                convert = {},
                updateInputText = {},
                updateOutputText = {},
                hideErrorCard = {},
                navigateScreen = {},
            )
        }
    }
}

class PreviewConverterUiStateProvider : PreviewParameterProvider<ConvertUiState> {
    override val values: Sequence<ConvertUiState> = sequenceOf(
        ConvertUiState(inputText = "漢字", outputText = "かんじ"),
        ConvertUiState(showErrorCard = true, convertErrorType = ConvertErrorType.REACHED_CONVERT_MAX_LIMIT),
    )
}
