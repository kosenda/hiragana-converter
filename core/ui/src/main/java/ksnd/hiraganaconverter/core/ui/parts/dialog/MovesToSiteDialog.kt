package ksnd.hiraganaconverter.core.ui.parts.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ksnd.hiraganaconverter.core.resource.R

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

// FIXME: AlertDialog does not yet support Showkase yet
//  ref: https://github.com/airbnb/Showkase/issues/235
//@UiModePreview
//@Composable
//fun PreviewMovesToSiteDialog() {
//    HiraganaConverterTheme(isDarkTheme = isSystemInDarkTheme()) {
//        MovesToSiteDialog(
//            onDismissRequest = {},
//            onClick = {},
//            url = "架空のURL",
//        )
//    }
//}
