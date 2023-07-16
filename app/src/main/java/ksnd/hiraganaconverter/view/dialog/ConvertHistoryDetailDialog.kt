package ksnd.hiraganaconverter.view.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.hiraganaconverter.view.parts.button.CustomIconButton
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryDetailDialog(
    onCloseClick: () -> Unit,
    historyData: ConvertHistoryData,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        ConvertHistoryDetailDialogContent(
            onCloseClick = onCloseClick,
            historyData = historyData,
        )
    }
}

@Composable
private fun ConvertHistoryDetailDialogContent(
    onCloseClick: () -> Unit,
    historyData: ConvertHistoryData,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.90f)
            .fillMaxWidth(0.90f)
            .clip(RoundedCornerShape(16.dp)),
        bottomBar = {
            BottomCloseButton(onClick = onCloseClick)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = historyData.time,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            )
            BeforeOrAfterText(
                historyData = historyData,
                isBefore = true,
                clipboardManager = clipboardManager,
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
            )
            BeforeOrAfterText(
                historyData = historyData,
                isBefore = false,
                clipboardManager = clipboardManager,
            )
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
                "[ ${stringResource(id = R.string.before_conversion)} ]"
            } else {
                "[ ${stringResource(id = R.string.after_conversion)} ]"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        CustomIconButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            contentDescription = "copyText",
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
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

@Preview
@Composable
private fun PreviewConvertHistoryDetailDialogContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            ConvertHistoryDetailDialogContent(
                onCloseClick = {},
                historyData = ConvertHistoryData(
                    id = 0,
                    time = "2022/11/26 22:25",
                    before = "変換前はこんな感じ",
                    after = "へんかんごはこんなかんじ",
                ),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConvertHistoryDetailDialogContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            ConvertHistoryDetailDialogContent(
                onCloseClick = {},
                historyData = ConvertHistoryData(
                    id = 0,
                    time = "2022/11/26 22:25",
                    before = "変換前はこんな感じ",
                    after = "へんかんごはこんなかんじ",
                ),
            )
        }
    }
}
