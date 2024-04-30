package ksnd.hiraganaconverter.core.ui.parts.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun DialogTopBar(
    isScrolled: Boolean,
    modifier: Modifier = Modifier,
    leftContent: @Composable RowScope.() -> Unit = {},
    onCloseClick: () -> Unit,
) {
    Column {
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
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = "",
                painter = painterResource(id = R.drawable.baseline_close_24),
                onClick = onCloseClick,
            )
        }
        AnimatedVisibility(
            visible = isScrolled,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500)),
        ) {
            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondaryContainer)
        }
    }
}

@UiModePreview
@Composable
fun PreviewDialogTopBar() {
    HiraganaConverterTheme {
        Surface {
            DialogTopBar(
                isScrolled = true,
                leftContent = {},
                onCloseClick = {},
            )
        }
    }
}
