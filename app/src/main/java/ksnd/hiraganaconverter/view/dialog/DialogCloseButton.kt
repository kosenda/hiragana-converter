package ksnd.hiraganaconverter.view.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.parts.button.CustomIconButton

@Composable
fun DialogCloseButton(
    modifier: Modifier = Modifier,
    leftContent: @Composable () -> Unit = {},
    onCloseClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leftContent()
        Spacer(modifier = Modifier.weight(1f))
        CustomIconButton(
            modifier = Modifier.padding(end = 8.dp),
            contentDescription = "",
            painter = painterResource(id = R.drawable.baseline_close_24),
            onClick = onCloseClick,
        )
    }
}
