package ksnd.hiraganaconverter.view.dialog

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
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.view.parts.button.DeleteButton
import ksnd.hiraganaconverter.view.parts.card.ConvertHistoryCard
import ksnd.hiraganaconverter.view.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.viewmodel.ConvertHistoryViewModel
import ksnd.hiraganaconverter.viewmodel.ConvertHistoryViewModelImpl
import ksnd.hiraganaconverter.viewmodel.PreviewConvertHistoryViewModel

@Composable
fun ConvertHistoryDialog(
    onCloseClick: () -> Unit,
    convertHistoryViewModel: ConvertHistoryViewModelImpl = hiltViewModel(),
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onCloseClick)
        ConvertHistoryDialogContent(
            onCloseClick = onCloseClick,
            viewModel = convertHistoryViewModel,
        )
    }
}

@Composable
private fun ConvertHistoryDialogContent(
    onCloseClick: () -> Unit,
    viewModel: ConvertHistoryViewModel,
) {
    val convertHistoryUiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
    ) {
        if (convertHistoryUiState.isShowDetailDialog) {
            convertHistoryUiState.usedHistoryDataByDetail?.let {
                ConvertHistoryDetailDialog(
                    onCloseClick = viewModel::closeConvertHistoryDetailDialog,
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
                    if (convertHistoryUiState.convertHistories.isNotEmpty()) {
                        DeleteButton(onClick = viewModel::deleteAllConvertHistory)
                    }
                },
                onCloseClick = onCloseClick,
            )
            if (convertHistoryUiState.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                LazyColumn {
                    items(
                        items = convertHistoryUiState.convertHistories,
                        key = { history -> history.id },
                    ) { history ->
                        ConvertHistoryCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            beforeText = history.before,
                            time = history.time,
                            onClick = {
                                viewModel.showConvertHistoryDetailDialog(history)
                            },
                            onDeleteClick = {
                                viewModel.deleteConvertHistory(history)
                            },
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
                onCloseClick = {},
                viewModel = PreviewConvertHistoryViewModel(),
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
                onCloseClick = {},
                viewModel = PreviewConvertHistoryViewModel(isNoData = true),
            )
        }
    }
}
