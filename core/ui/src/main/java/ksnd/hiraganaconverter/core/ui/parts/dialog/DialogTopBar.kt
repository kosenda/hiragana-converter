package ksnd.hiraganaconverter.core.ui.parts.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun DialogTopBar(
    modifier: Modifier = Modifier,
    leftContent: @Composable RowScope.() -> Unit = {},
    onCloseClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.weight(1f), content = leftContent)
        CustomIconButton(
            icon = R.drawable.baseline_close_24,
            contentDescription = "",
            modifier = Modifier.padding(end = 8.dp),
            onClick = onCloseClick,
        )
    }
}

@UiModePreview
@Composable
fun PreviewDialogTopBar() {
    HiraganaConverterTheme {
        Surface {
            DialogTopBar(
                leftContent = {},
                onCloseClick = {},
            )
        }
    }
}
