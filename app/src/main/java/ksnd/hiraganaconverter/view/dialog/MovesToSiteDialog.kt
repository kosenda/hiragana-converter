package ksnd.hiraganaconverter.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun MovesToSiteDialog(onDismissRequest: () -> Unit, onClick: () -> Unit, url: String) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = R.string.move_to_browser)) },
        text = { Text(text = url) },
        confirmButton = {
            TextButton(onClick = onClick) {
                Text(
                    text = "OK",
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "Cancel",
                )
            }
        },
    )
}

@Preview
@Composable
private fun PreviewMovesToSiteDialog_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        MovesToSiteDialog(
            onDismissRequest = {},
            onClick = {},
            url = "架空のURL",
        )
    }
}

@Preview
@Composable
private fun PreviewMovesToSiteDialog_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        MovesToSiteDialog(
            onDismissRequest = {},
            onClick = {},
            url = "架空のURL",
        )
    }
}
