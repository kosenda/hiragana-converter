package ksnd.hiraganaconverter.view.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.hiraganaconverter.view.parts.button.DeleteButton
import ksnd.hiraganaconverter.view.parts.card.ConvertHistoryCard
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
        ConvertHistoryDialogContent(
            onCloseClick = onCloseClick,
            viewModel = convertHistoryViewModel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConvertHistoryDialogContent(
    onCloseClick: () -> Unit,
    viewModel: ConvertHistoryViewModel,
) {
    val convertHistoryUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAllConvertHistory()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(16.dp)),
        bottomBar = {
            BottomCloseButton(onClick = onCloseClick)
        },
    ) { padding ->

        if (convertHistoryUiState.isShowDetailDialog) {
            convertHistoryUiState.usedHistoryDataByDetail?.let {
                ConvertHistoryDetailDialog(
                    onCloseClick = viewModel::closeConvertHistoryDetailDialog,
                    historyData = it,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp)
                .fillMaxSize(),
        ) {
            if (convertHistoryUiState.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    DeleteButton(
                        onClick = viewModel::deleteAllConvertHistory,
                    )
                }
                LazyColumn {
                    items(
                        items = convertHistoryUiState.convertHistories,
                        key = { history -> history.id },
                    ) { history ->
                        ConvertHistoryCard(
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

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
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

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
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

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent_NoData_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
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

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent_NoData_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
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
