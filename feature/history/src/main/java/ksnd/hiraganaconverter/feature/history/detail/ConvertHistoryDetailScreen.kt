package ksnd.hiraganaconverter.feature.history.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.model.mock.MockConvertHistoryData
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.MoveTopButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ConvertHistoryDetailScreen(
    historyData: ConvertHistoryData,
    onBackPressed: () -> Unit,
) {
    ConvertHistoryDetailScreenContent(
        historyData = historyData,
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConvertHistoryDetailScreenContent(
    historyData: ConvertHistoryData,
    onBackPressed: () -> Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    var moveTopButtonHeight by remember { mutableIntStateOf(0) }
    var topBarHeight by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.title_detail),
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .noRippleClickable {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                    .onSizeChanged { topBarHeight = it.height },
                onBackPressed = onBackPressed,
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
fun PreviewConvertHistoryDetailScreenContent() {
    HiraganaConverterTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ConvertHistoryDetailScreenContent(
                historyData = MockConvertHistoryData().data[0],
                onBackPressed = {},
            )
        }
    }
}
