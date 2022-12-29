package ksnd.open.hiraganaconverter.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.open.hiraganaconverter.view.parts.button.DeleteButton
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.open.hiraganaconverter.viewmodel.ConvertHistoryViewModel
import ksnd.open.hiraganaconverter.viewmodel.ConvertHistoryViewModelImpl
import ksnd.open.hiraganaconverter.viewmodel.PreviewConvertHistoryViewModel

@OptIn(ExperimentalComposeUiApi::class)
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
                .fillMaxSize(),
        ) {
            if (convertHistoryUiState.convertHistories.isEmpty()) {
                EmptyHistoryImage()
            } else {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
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
private fun ConvertHistoryCard(
    beforeText: String,
    time: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    OutlinedCard(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .scale(scale = buttonScaleState.animationScale.value)
            .clickable(
                interactionSource = buttonScaleState.interactionSource,
                indication = null,
                onClick = onClick,
            ),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .weight(1f),
            ) {
                ConvertHistoryCardTimeText(timeText = time)
                Text(
                    text = beforeText,
                    maxLines = 2,
                    minLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            FilledTonalIconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                ),
            ) {
                Image(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "delete convert history",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        }
    }
}

@Composable
private fun ConvertHistoryCardTimeText(timeText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = timeText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.weight(1f))
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
