package ksnd.hiraganaconverter.feature.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.DeleteButton
import ksnd.hiraganaconverter.core.ui.parts.card.ConvertHistoryCard
import ksnd.hiraganaconverter.core.ui.parts.dialog.DialogCloseButton
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryDialog(
    onCloseClick: () -> Unit,
    viewModel: ConvertHistoryViewModel,
) {
    val uiState by viewModel.uiState.collectAsState(ConvertHistoryUiState())

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onCloseClick)
        ConvertHistoryDialogContent(
            state = uiState,
            onCloseClick = onCloseClick,
            closeConvertHistoryDetailDialog = viewModel::closeConvertHistoryDetailDialog,
            deleteAllConvertHistory = viewModel::deleteAllConvertHistory,
            showConvertHistoryDetailDialog = viewModel::showConvertHistoryDetailDialog,
            deleteConvertHistory = viewModel::deleteConvertHistory,
        )
    }
}

@Composable
private fun ConvertHistoryDialogContent(
    state: ConvertHistoryUiState,
    onCloseClick: () -> Unit,
    closeConvertHistoryDetailDialog: () -> Unit,
    deleteAllConvertHistory: () -> Unit,
    showConvertHistoryDetailDialog: (ConvertHistoryData) -> Unit,
    deleteConvertHistory: (ConvertHistoryData) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
    ) {
        if (state.isShowDetailDialog) {
            state.usedHistoryDataByDetail?.let {
                ConvertHistoryDetailDialog(
                    onCloseClick = closeConvertHistoryDetailDialog,
                    historyData = it,
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DialogCloseButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                leftContent = {
                    if (state.convertHistories.isNotEmpty()) {
                        DeleteButton(onClick = deleteAllConvertHistory)
                    }
                },
                onCloseClick = onCloseClick,
            )
            if (state.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                LazyColumn {
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
private fun PreviewConvertHistoryDialogContent() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryDialogContent(
                state = ConvertHistoryUiState(convertHistories = MockConvertHistories().data),
                onCloseClick = {},
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
private fun PreviewConvertHistoryDialogContent_NoData() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
        ) {
            ConvertHistoryDialogContent(
                state = ConvertHistoryUiState(),
                onCloseClick = {},
                closeConvertHistoryDetailDialog = {},
                deleteAllConvertHistory = {},
                showConvertHistoryDetailDialog = {},
                deleteConvertHistory = {},
            )
        }
    }
}
