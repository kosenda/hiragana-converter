package ksnd.hiraganaconverter.core.ui.parts.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun MoveToBrowserDialog(
    url: String,
    onMoveToBrowser: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val urlHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = R.string.move_to_browser)) },
        text = { Text(text = url) },
        confirmButton = {
            TextButton(
                onClick = {
                    urlHandler.openUri(url)
                    onMoveToBrowser()
                },
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )
}

@UiModePreview
@Composable
fun PreviewMoveToBrowserDialog() {
    HiraganaConverterTheme {
        MoveToBrowserDialog(
            onDismissRequest = {},
            onMoveToBrowser = {},
            url = "https://example.com",
        )
    }
}
