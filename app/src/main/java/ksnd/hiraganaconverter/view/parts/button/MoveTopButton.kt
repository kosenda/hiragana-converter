package ksnd.hiraganaconverter.view.parts.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.view.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun MoveTopButton(scrollState: ScrollState) {
    val scrollScope = rememberCoroutineScope()
    val offset = IntOffset(x = 100, y = 100)
    val showVisibleTopBar by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    AnimatedVisibility(
        visible = showVisibleTopBar,
        enter = scaleIn() + slideIn(initialOffset = { offset }),
        exit = scaleOut() + slideOut(targetOffset = { offset }),
    ) {
        FloatingActionButton(
            onClick = {
                scrollScope.launch {
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

@UiModeAndLocalePreview
@Composable
private fun PreviewMoveTopButton_() {
    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
        val scrollState = rememberScrollState(initial = 1)
        Box(
            modifier = Modifier.padding(all = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            MoveTopButton(scrollState = scrollState)
        }
    }
}
