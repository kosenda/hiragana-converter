package ksnd.hiraganaconverter.feature.history.detail

import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import ksnd.hiraganaconverter.core.ui.ConvertHistorySharedKey
import ksnd.hiraganaconverter.core.ui.SharedType
import ksnd.hiraganaconverter.core.ui.extension.easySharedBounds
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.MoveTopButton
import ksnd.hiraganaconverter.core.ui.preview.SharedTransitionAndAnimatedVisibilityProvider
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.history.ConvertHistoryTimeText
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ConvertHistoryDetailScreen(
    history: ConvertHistoryData,
    onBackPressed: () -> Unit,
) {
    ConvertHistoryDetailScreenContent(
        history = history,
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun ConvertHistoryDetailScreenContent(
    history: ConvertHistoryData,
    onBackPressed: () -> Unit,
) {
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
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            ) {
                Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
                    Text(
                        text = stringResource(id = R.string.time),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .alignByBaseline(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    ConvertHistoryTimeText(
                        history = history,
                        modifier = Modifier.alignByBaseline(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                BeforeOrAfterText(
                    history = history,
                    isBefore = true,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
                BeforeOrAfterText(
                    history = history,
                    isBefore = false,
                )
                Spacer(modifier = Modifier.height(48.dp + (moveTopButtonHeight / LocalDensity.current.density).toInt().dp))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BeforeOrAfterText(
    history: ConvertHistoryData,
    isBefore: Boolean,
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Text(
            text = if (isBefore) {
                String.format("〈 %s 〉", stringResource(id = R.string.before_conversion))
            } else {
                String.format("〈 %s 〉", stringResource(id = R.string.after_conversion))
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        CustomIconButton(
            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
            contentDescription = "",
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(
                        text = if (isBefore) {
                            history.before
                        } else {
                            history.after
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
                history.before
            } else {
                history.after
            },
            style = MaterialTheme.typography.titleMedium,
            color = if (isBefore) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.tertiary
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .defaultMinSize(minHeight = 120.dp)
                .fillMaxWidth()
                .then(
                    if (isBefore) {
                        Modifier.easySharedBounds(ConvertHistorySharedKey(id = history.id, type = SharedType.BEFORE_TEXT))
                    } else {
                        Modifier
                    },
                ),
        )
    }
}

@UiModePreview
@Composable
fun PreviewConvertHistoryDetailScreenContent() {
    HiraganaConverterTheme {
        SharedTransitionAndAnimatedVisibilityProvider {
            Box(modifier = Modifier.fillMaxSize()) {
                ConvertHistoryDetailScreenContent(
                    history = MockConvertHistoryData().data[0],
                    onBackPressed = {},
                )
            }
        }
    }
}
