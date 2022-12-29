package ksnd.open.hiraganaconverter.view.parts.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.R

@OptIn(ExperimentalAnimationApi::class)
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
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = stringResource(id = R.string.top),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
