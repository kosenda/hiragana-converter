package ksnd.hiraganaconverter.feature.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard

private const val FLOATING_PADDING = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyContent() {
    var isShowWebView by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val privacyPolicyUrl = stringResource(id = R.string.privacy_policy_url)
    var floatingPlayerHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current.density
    val webViewState = rememberWebViewState(privacyPolicyUrl)
    val coroutineScope = rememberCoroutineScope()
    val navigator = rememberWebViewNavigator()
    val scrollState = rememberScrollState()
    val isScrollTop by remember(scrollState.value) { derivedStateOf { scrollState.value == 0 } }

    TitleCard(
        text = stringResource(id = R.string.privacy_policy_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )

    TransitionButton(
        text = stringResource(id = R.string.privacy_policy_button),
        onClick = { isShowWebView = true },
    )

    if (isShowWebView) {
        ModalBottomSheet(
            onDismissRequest = { isShowWebView = false },
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    BottomSheetDefaults.DragHandle()
                    Row {
                        CustomIconButton(
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_left_24),
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
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
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
            },
            sheetState = sheetState,
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
                    this@ModalBottomSheet.AnimatedVisibility(visible = isScrollTop.not()) {
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(all = FLOATING_PADDING.dp)
                                .onSizeChanged {
                                    floatingPlayerHeight = it.height / density.toInt()
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
                }
            }
        }
    }
}
