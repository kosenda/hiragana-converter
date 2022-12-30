package ksnd.open.hiraganaconverter.view.parts.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun BottomCloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
            .height(48.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary),
        onClick = onClick,
        interactionSource = buttonScaleState.interactionSource,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "close button",
            modifier = Modifier
                .padding(end = 16.dp)
                .size(36.dp),
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Text(
            text = stringResource(id = R.string.close),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Preview
@Composable
private fun PreviewBottomComposableButton_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            BottomCloseButton {}
        }
    }
}

@Preview
@Composable
private fun PreviewBottomComposableButton_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            BottomCloseButton {}
        }
    }
}
