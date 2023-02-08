package ksnd.open.hiraganaconverter.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class ButtonScaleState {
    var interactionSource by mutableStateOf(MutableInteractionSource())
    var animationScale by mutableStateOf(Animatable(1f))
}

@Composable
fun rememberButtonScaleState(): ButtonScaleState {
    val buttonScaleState = remember { ButtonScaleState() }
    LaunchedEffect(buttonScaleState.interactionSource) {
        buttonScaleState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    buttonScaleState.animationScale.animateTo(
                        targetValue = 0.95f,
                        animationSpec = tween(durationMillis = 100),
                    )
                }
                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    buttonScaleState.animationScale.animateTo(
                        targetValue = 1.0f,
                        animationSpec = tween(durationMillis = 500),
                    )
                }
            }
        }
    }
    return buttonScaleState
}
