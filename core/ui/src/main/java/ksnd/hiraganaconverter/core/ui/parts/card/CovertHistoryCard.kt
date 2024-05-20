package ksnd.hiraganaconverter.core.ui.parts.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun ConvertHistoryCard(
    modifier: Modifier = Modifier,
    beforeText: String,
    time: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .weight(1f),
            ) {
                ConvertHistoryCardTimeText(timeText = time)
                Text(
                    text = beforeText,
                    maxLines = 2,
                    minLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            FilledTonalIconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                ),
            ) {
                Image(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "delete convert history",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        }
    }
}

@Composable
private fun ConvertHistoryCardTimeText(timeText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = timeText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@UiModePreview
@Composable
fun PreviewConvertHistoryCard() {
    HiraganaConverterTheme {
        ConvertHistoryCard(
            beforeText = "変換前文章",
            time = "2022/12/29 18:19",
            onClick = {},
            onDeleteClick = {},
        )
    }
}
