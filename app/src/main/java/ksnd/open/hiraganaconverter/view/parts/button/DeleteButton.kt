package ksnd.open.hiraganaconverter.view.parts.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.rememberButtonScaleState
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val buttonScaleState = rememberButtonScaleState()
    OutlinedButton(
        modifier = modifier
            .padding(all = 8.dp)
            .height(48.dp)
            .scale(scale = buttonScaleState.animationScale.value),
        onClick = onClick,
        interactionSource = buttonScaleState.interactionSource,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.error),
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.error,
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = stringResource(id = R.string.delete_all),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDeleteButton_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DeleteButton(onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewDeleteButton_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DeleteButton(onClick = {})
        }
    }
}
