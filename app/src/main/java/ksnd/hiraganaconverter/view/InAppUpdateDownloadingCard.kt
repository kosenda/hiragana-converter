package ksnd.hiraganaconverter.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ksnd.hiraganaconverter.core.resource.R

@Composable
fun InAppUpdateDownloadingCard(text: String, isVisible: Boolean) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation_downloading),
    )
    val lottieAnimationColor = MaterialTheme.colorScheme.primary
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(1000)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(1000)),
    ) {
        Card(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(color = lottieAnimationColor, blendMode = BlendMode.SrcAtop)
                            }
                        },
                    iterations = LottieConstants.IterateForever,
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(all = 16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
