package ksnd.hiraganaconverter.feature.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.TitleWithIcon
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

private const val FLOATING_PADDING = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyContent() {
    var isShowWebView by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var floatingPlayerHeight by remember { mutableIntStateOf(0) }
    val webViewState = rememberWebViewState(url = stringResource(id = R.string.privacy_policy_url))
    val navigator = rememberWebViewNavigator()
    val scrollState = rememberScrollState()
    val analytics = LocalAnalytics.current

    LaunchedEffect(isShowWebView) {
        if (isShowWebView) analytics.logScreen(Screen.PRIVACY_POLICY)
    }

    TitleWithIcon(
        title = R.string.privacy_policy_title,
        icon = R.drawable.ic_outline_info_24,
        modifier = Modifier.padding(top = 28.dp, bottom = 4.dp),
    )

    TransitionButton(
        text = stringResource(id = R.string.privacy_policy_button),
        onClick = { isShowWebView = true },
    )

    if (isShowWebView) {
        ModalBottomSheet(
            onDismissRequest = { isShowWebView = false },
            dragHandle = { DragHandle(navigator = navigator) },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
            ) {
                WebView(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(bottom = floatingPlayerHeight.dp + FLOATING_PADDING.dp),
                    state = webViewState,
                    navigator = navigator,
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    this@ModalBottomSheet.AnimatedVisibility(visible = scrollState.canScrollBackward) {
                        ToTopFloatingButton(
                            scrollState = scrollState,
                            sizeChangedHeight = { floatingPlayerHeight = it },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DragHandle(navigator: WebViewNavigator) {
    Box(
        modifier = Modifier
            .padding(all = 4.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        BottomSheetDefaults.DragHandle()
        Row {
            CustomIconButton(
                icon = R.drawable.baseline_keyboard_arrow_left_24,
                contentDescription = "",
                contentColor = if (navigator.canGoBack) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                },
                containerColor = Color.Transparent,
                onClick = navigator::navigateBack,
            )
            CustomIconButton(
                icon = R.drawable.baseline_keyboard_arrow_right_24,
                contentDescription = "",
                contentColor = if (navigator.canGoForward) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                },
                containerColor = Color.Transparent,
                onClick = navigator::navigateForward,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ToTopFloatingButton(scrollState: ScrollState, sizeChangedHeight: (Int) -> Unit) {
    val density = LocalDensity.current.density
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        modifier = Modifier
            .padding(all = FLOATING_PADDING.dp)
            .onSizeChanged {
                sizeChangedHeight(it.height / density.toInt())
            },
        onClick = {
            coroutineScope.launch {
                scrollState.animateScrollTo(0)
            }
        },
    ) {
        Image(
            modifier = Modifier.size(size = 48.dp),
            painter = painterResource(R.drawable.ic_baseline_keyboard_arrow_up_24),
            contentDescription = "TOP",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentScale = ContentScale.Fit,
        )
    }
}

@UiModePreview
@Composable
fun PreviewPrivacyPolicyContent() {
    HiraganaConverterTheme {
        PrivacyPolicyContent()
    }
}

@UiModePreview
@Composable
fun PreviewDragHandler() {
    HiraganaConverterTheme {
        DragHandle(navigator = rememberWebViewNavigator())
    }
}

@UiModePreview
@Composable
fun PreviewToTopFloatingPlayer() {
    HiraganaConverterTheme {
        ToTopFloatingButton(scrollState = rememberScrollState(), sizeChangedHeight = {})
    }
}
