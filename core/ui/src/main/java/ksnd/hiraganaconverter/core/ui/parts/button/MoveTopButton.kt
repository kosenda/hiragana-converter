package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun MoveTopButton(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current
    val offset = IntOffset(x = 100, y = 100)

    AnimatedVisibility(
        visible = scrollState.canScrollBackward,
        modifier = modifier.padding(
            start = WindowInsets.displayCutout
                .asPaddingValues()
                .calculateStartPadding(layoutDirection),
            end = WindowInsets.displayCutout
                .asPaddingValues()
                .calculateEndPadding(layoutDirection),
        ),
        enter = scaleIn() + slideIn(initialOffset = { offset }),
        exit = scaleOut() + slideOut(targetOffset = { offset }),
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                contentDescription = stringResource(id = R.string.top),
                modifier = Modifier.size(48.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewMoveTopButton_() {
    HiraganaConverterTheme {
        val scrollState = rememberScrollState(initial = 1)
        Box(
            modifier = Modifier.padding(all = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            MoveTopButton(scrollState = scrollState)
        }
    }
}
