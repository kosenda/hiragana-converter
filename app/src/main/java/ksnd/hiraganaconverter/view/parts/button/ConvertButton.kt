package ksnd.hiraganaconverter.view.parts.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.rememberButtonScaleState
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun ConvertButton(onClick: () -> Unit) {
    val buttonScaleState = rememberButtonScaleState()
    Button(
        modifier = Modifier
            .padding(all = 8.dp)
            .height(56.dp)
            .scale(scale = buttonScaleState.animationScale.value)
            .shadow(elevation = 4.dp, shape = CircleShape),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        interactionSource = buttonScaleState.interactionSource,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_compare_arrows_24),
                contentDescription = "convert",
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.tertiary,
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = stringResource(id = R.string.conversion),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConvertButton_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ConvertButton {}
        }
    }
}

@Preview
@Composable
private fun PreviewConvertButton_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ConvertButton {}
        }
    }
}
