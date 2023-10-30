package ksnd.hiraganaconverter.feature.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.DeleteButton
import ksnd.hiraganaconverter.core.ui.parts.card.ConvertHistoryCard
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryScreen(
    viewModel: ConvertHistoryViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState(ConvertHistoryUiState())
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.HISTORY)
    }

    ConvertHistoryScreenContent(
        state = uiState,
        onBackPressed = onBackPressed,
        closeConvertHistoryDetailDialog = viewModel::closeConvertHistoryDetailDialog,
        deleteAllConvertHistory = viewModel::deleteAllConvertHistory,
        showConvertHistoryDetailDialog = viewModel::showConvertHistoryDetailDialog,
        deleteConvertHistory = viewModel::deleteConvertHistory,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConvertHistoryScreenContent(
    state: ConvertHistoryUiState,
    onBackPressed: () -> Unit,
    closeConvertHistoryDetailDialog: () -> Unit,
    deleteAllConvertHistory: () -> Unit,
    showConvertHistoryDetailDialog: (ConvertHistoryData) -> Unit,
    deleteConvertHistory: (ConvertHistoryData) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            BackTopBar(scrollBehavior = scrollBehavior, onBackPressed = onBackPressed)
        }
    ) { padding ->
        if (state.isShowDetailDialog) {
            state.usedHistoryDataByDetail?.let {
                ConvertHistoryDetailDialog(
                    onCloseClick = closeConvertHistoryDetailDialog,
                    historyData = it,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            if (state.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                LazyColumn(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    item {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            DeleteButton(
                                modifier = Modifier.padding(end = 16.dp),
                                onClick = deleteAllConvertHistory
                            )
                        }
                    }
                    items(
                        items = state.convertHistories,
                        key = { history -> history.id },
                    ) { history ->
                        ConvertHistoryCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            beforeText = history.before,
                            time = history.time,
                            onClick = { showConvertHistoryDetailDialog(history) },
                            onDeleteClick = { deleteConvertHistory(history) },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
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

@UiModeAndLocalePreview
@Composable
private fun PreviewConvertHistoryScreeContent() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryScreenContent(
                state = ConvertHistoryUiState(convertHistories = MockConvertHistories().data),
                onBackPressed = {},
                closeConvertHistoryDetailDialog = {},
                deleteAllConvertHistory = {},
                showConvertHistoryDetailDialog = {},
                deleteConvertHistory = {},
            )
        }
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewConvertHistoryScreenContent_NoData() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryScreenContent(
                state = ConvertHistoryUiState(),
                onBackPressed = {},
                closeConvertHistoryDetailDialog = {},
                deleteAllConvertHistory = {},
                showConvertHistoryDetailDialog = {},
                deleteConvertHistory = {},
            )
        }
    }
}
