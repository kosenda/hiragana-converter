package ksnd.hiraganaconverter.feature.history

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.model.mock.MockConvertHistoryData
import ksnd.hiraganaconverter.core.ui.ConvertHistorySharedKey
import ksnd.hiraganaconverter.core.ui.SharedType
import ksnd.hiraganaconverter.core.ui.extension.easySharedBounds
import ksnd.hiraganaconverter.core.ui.preview.SharedTransitionAndAnimatedVisibilityProvider

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ConvertHistoryTimeText(
    history: ConvertHistoryData,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    Text(
        modifier = modifier.easySharedBounds(ConvertHistorySharedKey(id = history.id, type = SharedType.CONVERT_TIME)),
        text = history.time,
        style = style,
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Preview
@Composable
fun PreviewConvertHistoryTimeText() {
    SharedTransitionAndAnimatedVisibilityProvider {
        ConvertHistoryTimeText(history = MockConvertHistoryData().data.first())
    }
}
