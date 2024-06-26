package ksnd.hiraganaconverter.feature.history

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.MoveTopButton
import ksnd.hiraganaconverter.core.ui.parts.dialog.DialogTopBar
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ConvertHistoryDetailDialog(
    onCloseClick: () -> Unit,
    historyData: ConvertHistoryData,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onCloseClick)
        ConvertHistoryDetailDialogContent(
            historyData = historyData,
            onCloseClick = onCloseClick,
        )
    }
}

@Composable
private fun ConvertHistoryDetailDialogContent(
    historyData: ConvertHistoryData,
    onCloseClick: () -> Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    var moveTopButtonHeight by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        topBar = {
            DialogTopBar(
                isScrolled = scrollState.canScrollBackward,
                leftContent = {
                    Text(
                        text = historyData.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    )
                },
                onCloseClick = onCloseClick,
            )
        },
        floatingActionButton = {
            MoveTopButton(
                scrollState = scrollState,
                modifier = Modifier.onSizeChanged { moveTopButtonHeight = it.height },
            )
        },
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
                    .verticalScroll(scrollState)
                    .padding(innerPadding),
            ) {
                BeforeOrAfterText(
                    historyData = historyData,
                    isBefore = true,
                    clipboardManager = clipboardManager,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
                BeforeOrAfterText(
                    historyData = historyData,
                    isBefore = false,
                    clipboardManager = clipboardManager,
                )
                Spacer(modifier = Modifier.height(48.dp + (moveTopButtonHeight / LocalDensity.current.density).toInt().dp))
            }
        }
    }
}

@Composable
private fun BeforeOrAfterText(
    historyData: ConvertHistoryData,
    isBefore: Boolean,
    clipboardManager: ClipboardManager,
) {
    val context = LocalContext.current
    Row {
        Text(
            text = if (isBefore) {
                String.format("〈 %s 〉", stringResource(id = R.string.before_conversion))
            } else {
                String.format("〈 %s 〉", stringResource(id = R.string.after_conversion))
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        CustomIconButton(
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
            contentDescription = "",
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(
                        text = if (isBefore) {
                            historyData.before
                        } else {
                            historyData.after
                        },
                    ),
                )
                Toast
                    .makeText(context, "COPIED.", Toast.LENGTH_SHORT)
                    .show()
            },
        )
    }
    SelectionContainer {
        Text(
            text = if (isBefore) {
                historyData.before
            } else {
                historyData.after
            },
            style = MaterialTheme.typography.titleMedium,
            color = if (isBefore) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.tertiary
            },
            modifier = Modifier
                .padding(all = 16.dp)
                .defaultMinSize(minHeight = 120.dp)
                .fillMaxWidth(),
        )
    }
}

@UiModePreview
@Composable
fun PreviewConvertHistoryDetailDialogContent() {
    HiraganaConverterTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ConvertHistoryDetailDialogContent(
                historyData = ConvertHistoryData(
                    id = 0,
                    time = "2022/11/26 22:25",
                    before = "変換前はこんな感じ",
                    after = "へんかんごはこんなかんじ",
                ),
                onCloseClick = {},
            )
        }
    }
}
