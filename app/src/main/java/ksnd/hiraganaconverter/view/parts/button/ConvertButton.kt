package ksnd.hiraganaconverter.view.parts.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ksnd.hiraganaconverter.R

@Composable
fun ConvertButton(
    modifier: Modifier = Modifier,
    isConverting: Boolean,
    @DrawableRes id: Int,
    convertDescription: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation_loading),
    )
    if (isConverting) {
        Box(
            modifier = Modifier
                .padding(all = 8.dp)
                .size(size = 56.dp)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center,
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }
    } else {
        CustomButtonWithBackground(
            modifier = modifier,
            id = id,
            convertDescription = convertDescription,
            containerColor = containerColor,
            contentColor = contentColor,
            onClick = onClick,
        )
    }
}
