package ksnd.hiraganaconverter.feature.history

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.model.mock.MockConvertHistoryData
import ksnd.hiraganaconverter.core.ui.ConvertHistorySharedKey
import ksnd.hiraganaconverter.core.ui.SharedType
import ksnd.hiraganaconverter.core.ui.extension.easySharedBounds
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.preview.SharedTransitionAndAnimatedVisibilityProvider
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ConvertHistoryCard(
    modifier: Modifier = Modifier,
    history: ConvertHistoryData,
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
                ConvertHistoryTimeText(
                    history = history,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = history.before,
                    modifier = Modifier.easySharedBounds(ConvertHistorySharedKey(id = history.id, type = SharedType.BEFORE_TEXT)),
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

@UiModePreview
@Composable
fun PreviewConvertHistoryCard() {
    SharedTransitionAndAnimatedVisibilityProvider {
        HiraganaConverterTheme {
            ConvertHistoryCard(
                history = MockConvertHistoryData().data[0],
                onClick = {},
                onDeleteClick = {},
            )
        }
    }
}
