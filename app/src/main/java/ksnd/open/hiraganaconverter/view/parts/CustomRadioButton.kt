package ksnd.open.hiraganaconverter.view.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.CustomFont

@Composable
fun CustomFontRadioButton(
    onClick: () -> Unit,
    selected: Boolean,
    text: String,
    fontFamily: FontFamily?,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(40.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fontFamily,
        )
        RadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(),
            onClick = onClick,
        )
    }
}

@Composable
fun CustomThemeRadioButton(
    isSelected: Boolean,
    buttonText: String,
    painter: Painter,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(40.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painter,
            contentDescription = buttonText,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = buttonText,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        RadioButton(
            selected = isSelected,
            colors = RadioButtonDefaults.colors(),
            onClick = onClick,
        )
    }
}

@Preview
@Composable
private fun PreviewCustomFontRadioButton() {
    CustomFontRadioButton(
        onClick = {},
        selected = true,
        text = CustomFont.DEFAULT.name,
        fontFamily = FontFamily.Default,
    )
}

@Preview
@Composable
private fun PreviewCustomThemeRadioButton() {
    CustomThemeRadioButton(
        buttonText = stringResource(id = R.string.dark_mode),
        isSelected = true,
        painter = painterResource(id = R.drawable.ic_baseline_brightness_2_24),
        onClick = {},
    )
}
