package ksnd.hiraganaconverter.feature.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.DeleteButton
import ksnd.hiraganaconverter.core.ui.parts.card.ConvertHistoryCard
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryScreen(
    viewModel: ConvertHistoryViewModel,
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
        showConvertHistoryDetailDialog = viewModel::showConvertHistoryDetailDialog,
        deleteConvertHistory = viewModel::deleteConvertHistory,
    )

    if (uiState.isShowDetailDialog) {
        uiState.usedHistoryDataByDetail?.let {
            ConvertHistoryDetailDialog(
                onCloseClick = viewModel::closeConvertHistoryDetailDialog,
                historyData = it,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ConvertHistoryScreenContent(
    state: ConvertHistoryUiState,
    onBackPressed: () -> Unit,
    deleteAllConvertHistory: () -> Unit,
    showConvertHistoryDetailDialog: (ConvertHistoryData) -> Unit,
    deleteConvertHistory: (ConvertHistoryData) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val layoutDirection = LocalLayoutDirection.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface)
            .displayCutoutPadding(),
        topBar = {
            BackTopBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.noRippleClickable {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                onBackPressed = onBackPressed,
            ) {
                if (state.convertHistories.isNotEmpty()) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        DeleteButton(
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = deleteAllConvertHistory,
                        )
                    }
                }
            }
        }
},
) {
    padding ->
    Column(
        modifier = Modifier
            .padding(
                paddingValues = PaddingValues(
                    start = padding.calculateStartPadding(layoutDirection),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(layoutDirection),
                ),
            )
            .fillMaxSize(),
    ) {
        if (state.convertHistories.isEmpty()) {
            EmptyHistoryImage()
        } else {
            LazyColumn(
                state = lazyListState,
            ) {
                items(
                    items = state.convertHistories,
                    key = { history -> history.id },
                ) { history ->
                    ConvertHistoryCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .then(
                                // adding the condition because it behaves strangely when set while scrolling
                                if (lazyListState.isScrollInProgress) Modifier else Modifier.animateItemPlacement(),
                            ),
                        beforeText = history.before,
                        time = history.time,
                        onClick = { showConvertHistoryDetailDialog(history) },
                        onDeleteClick = { deleteConvertHistory(history) },
                    )
                }
                item { Spacer(modifier = Modifier.height(48.dp)) }
            }
        }
    }
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
                contentDescription = "no data",
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

@UiModePreview
@Composable
fun PreviewConvertHistoryScreeContent() {
    HiraganaConverterTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryScreenContent(
                state = ConvertHistoryUiState(convertHistories = MockConvertHistories().data),
                onBackPressed = {},
                deleteAllConvertHistory = {},
                showConvertHistoryDetailDialog = {},
                deleteConvertHistory = {},
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewConvertHistoryScreenContent_NoData() {
    HiraganaConverterTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryScreenContent(
                state = ConvertHistoryUiState(),
                onBackPressed = {},
                deleteAllConvertHistory = {},
                showConvertHistoryDetailDialog = {},
                deleteConvertHistory = {},
            )
        }
    }
}
