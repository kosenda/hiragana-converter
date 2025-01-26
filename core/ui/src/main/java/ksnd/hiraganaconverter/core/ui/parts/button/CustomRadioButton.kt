package ksnd.hiraganaconverter.core.ui.parts.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun CustomRadioButton(
    isSelected: Boolean,
    buttonText: String,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        icon?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(24.dp),
            )
        }
        Text(
            text = buttonText,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick,
        )
    }
}

@UiModePreview
@Composable
fun PreviewCustomThemeRadioButton() {
    HiraganaConverterTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
            CustomRadioButton(
                isSelected = true,
                buttonText = stringResource(R.string.dark_mode),
                icon = R.drawable.ic_baseline_brightness_2_24,
                onClick = {},
            )
        }
    }
}
