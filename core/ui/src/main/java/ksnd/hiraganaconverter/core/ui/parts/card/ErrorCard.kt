package ksnd.hiraganaconverter.core.ui.parts.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

const val ErrorCardAnimationDuration = 500

@Composable
fun ErrorCard(
    visible: Boolean,
    errorText: String,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(ErrorCardAnimationDuration)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(ErrorCardAnimationDuration)),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .clip(CircleShape)
                .scale(scale = buttonScaleState.animationScale.value)
                .noRippleClickable(
                    interactionSource = buttonScaleState.interactionSource,
                    onClick = onClick,
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            ),
        ) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                    contentDescription = "convert",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(36.dp),
                )
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

@UiModePreview
@Composable
fun PreviewErrorCard() {
    HiraganaConverterTheme {
        ErrorCard(visible = true, errorText = stringResource(id = R.string.limit_exceeded), onClick = {})
    }
}
