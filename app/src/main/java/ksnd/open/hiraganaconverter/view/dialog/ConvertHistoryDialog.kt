package ksnd.open.hiraganaconverter.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.view.parts.BottomCloseButton
import ksnd.open.hiraganaconverter.viewmodel.ConvertHistoryViewModel
import ksnd.open.hiraganaconverter.viewmodel.ConvertHistoryViewModelImpl
import ksnd.open.hiraganaconverter.viewmodel.PreviewConvertHistoryViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConvertHistoryDialog(
    onCloseClick: () -> Unit,
    convertHistoryViewModel: ConvertHistoryViewModelImpl = hiltViewModel()
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConvertHistoryDialogContent(
            onCloseClick = onCloseClick,
            viewModel = convertHistoryViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConvertHistoryDialogContent(
    onCloseClick: () -> Unit,
    viewModel: ConvertHistoryViewModel
) {
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
        }
    ) { padding ->

        var isShowDetail by rememberSaveable { mutableStateOf(false) }
        var showHistoryData by rememberSaveable {
            mutableStateOf<ConvertHistoryData?>(null)
        }

        if (isShowDetail) {
            showHistoryData?.let {
                ConvertHistoryDetailDialog(
                    onCloseClick = {
                        isShowDetail = false
                        showHistoryData = null
                    },
                    historyData = it
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (viewModel.convertHistories.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.desert),
                            contentDescription = "no data",
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(144.dp),
                            alignment = Alignment.Center,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "NO DATA",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    DeleteButton(
                        onClick = {
                            viewModel.deleteAllConvertHistory()
                            viewModel.convertHistories.value = emptyList()
                        }
                    )
                }
                LazyColumn {
                    items(
                        items = viewModel.convertHistories.value,
                        key = { history -> history.id }
                    ) { history ->
                        ConvertHistoryCard(
                            beforeText = history.before,
                            time = history.time,
                            onClick = {
                                isShowDetail = true
                                showHistoryData = history
                            },
                            onCloseClick = {
                                viewModel.deleteConvertHistory(history.id)
                            }
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
    onCloseClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = beforeText,
                    maxLines = 2,
                    minLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FilledTonalIconButton(
                onClick = onCloseClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "delete convert history",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    FilledTonalButton(
        modifier = Modifier
            .padding(all = 8.dp)
            .height(48.dp),
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onErrorContainer
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(id = R.string.delete_all),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        ConvertHistoryDialogContent(
            onCloseClick = {},
            viewModel = PreviewConvertHistoryViewModel()
        )
    }
}

@Preview
@Composable
private fun PreviewConvertHistoryDialogContent_NoData() {
    Box(modifier = Modifier.fillMaxSize()) {
        ConvertHistoryDialogContent(
            onCloseClick = {},
            viewModel = PreviewConvertHistoryViewModel(isNoData = true)
        )
    }
}
