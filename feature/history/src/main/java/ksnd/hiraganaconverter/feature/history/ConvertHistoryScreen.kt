package ksnd.hiraganaconverter.feature.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.model.mock.MockConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.SharedTransitionAndAnimatedVisibilityProvider
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryScreen(
    viewModel: ConvertHistoryViewModel,
    navigateHistoryDetail: (ConvertHistoryData) -> Unit,
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ConvertHistoryUiState())
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.HISTORY)
    }

    ConvertHistoryScreenContent(
        state = uiState,
        onBackPressed = onBackPressed,
        deleteAllConvertHistory = viewModel::deleteAllConvertHistory,
        deleteConvertHistory = viewModel::deleteConvertHistory,
        navigateHistoryDetail = navigateHistoryDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConvertHistoryScreenContent(
    state: ConvertHistoryUiState,
    onBackPressed: () -> Unit,
    deleteAllConvertHistory: () -> Unit,
    deleteConvertHistory: (ConvertHistoryData) -> Unit,
    navigateHistoryDetail: (ConvertHistoryData) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val layoutDirection = LocalLayoutDirection.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var topBarHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current.density
    val navigationHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var isShowConfirmDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.title_history),
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .noRippleClickable {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                    .onSizeChanged { topBarHeight = it.height },
                onBackPressed = onBackPressed,
            ) {
                if (state.convertHistories.isNotEmpty()) {
                    CustomIconButton(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                        contentDescription = "",
                        modifier = Modifier.padding(end = 16.dp),
                        contentColor = MaterialTheme.colorScheme.error,
                        onClick = { isShowConfirmDeleteDialog = true },
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(
                    start = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateEndPadding(layoutDirection),
                ),
        ) {
            if (state.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height((topBarHeight / density).toInt().dp))
                    }
                    items(
                        items = state.convertHistories,
                        key = { history -> history.id },
                    ) { history ->
                        ConvertHistoryCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            history = history,
                            onClick = { navigateHistoryDetail(history) },
                            onDeleteClick = { deleteConvertHistory(history) },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(48.dp + navigationHeight)) }
                }
            }
        }
    }

    if (isShowConfirmDeleteDialog) {
        AlertDialog(
            onDismissRequest = { isShowConfirmDeleteDialog = false },
            title = { Text(text = stringResource(id = R.string.delete_history_dialog_title)) },
            text = { Text(text = stringResource(id = R.string.delete_history_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteAllConvertHistory()
                        isShowConfirmDeleteDialog = false
                    },
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { isShowConfirmDeleteDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun EmptyHistoryImage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.desert),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(144.dp),
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
            Text(
                text = "NO DATA",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

class PreviewConverterHistoryUiStateProvider : PreviewParameterProvider<ConvertHistoryUiState> {
    override val values: Sequence<ConvertHistoryUiState> = sequenceOf(
        ConvertHistoryUiState(convertHistories = MockConvertHistoryData().data),
        ConvertHistoryUiState(),
    )
}

@UiModePreview
@Composable
fun PreviewConvertHistoryScreeContent(
    @PreviewParameter(PreviewConverterHistoryUiStateProvider::class) state: ConvertHistoryUiState,
) {
    HiraganaConverterTheme {
        SharedTransitionAndAnimatedVisibilityProvider {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxSize(),
            ) {
                ConvertHistoryScreenContent(
                    state = state,
                    onBackPressed = {},
                    deleteAllConvertHistory = {},
                    deleteConvertHistory = {},
                    navigateHistoryDetail = {},
                )
            }
        }
    }
}
