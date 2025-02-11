package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

private const val BUTTON_SIZE = 56

@Composable
fun ConvertButton(
    modifier: Modifier = Modifier,
    isConverting: Boolean,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    val haptics = LocalHapticFeedback.current
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_loading))
    val animateCornerRadius = animateDpAsState(targetValue = if (isConverting) 16.dp else (BUTTON_SIZE / 2).dp, label = "")

    Box(
        modifier = modifier
            .padding(all = 8.dp)
            .size(size = BUTTON_SIZE.dp)
            .clip(shape = RoundedCornerShape(size = animateCornerRadius.value))
            .scale(scale = buttonScaleState.animationScale.value)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable(
                interactionSource = buttonScaleState.interactionSource,
                indication = null,
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = isConverting,
            enter = fadeIn(animationSpec = tween(0)),
            exit = fadeOut(animationSpec = tween(500)),
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }

        AnimatedVisibility(
            visible = isConverting.not(),
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(0)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_compare_arrows_24),
                contentDescription = stringResource(id = R.string.conversion),
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewConvertButton() {
    HiraganaConverterTheme {
        ConvertButton(
            isConverting = false,
            onClick = {},
        )
    }
}
