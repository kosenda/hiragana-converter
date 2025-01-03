package ksnd.hiraganaconverter.core.ui.parts.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@Composable
fun RequestReviewDialog(onLater: () -> Unit, onOk: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(id = R.string.request_review_title),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.request_review_desc),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        },
        confirmButton = {
            TextButton(onClick = onOk) {
                Text(
                    text = stringResource(id = R.string.ok),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onLater) {
                Text(
                    text = stringResource(id = R.string.request_review_cancel),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    )
}

@UiModePreview
@Composable
fun PreviewRequestReviewDialog() {
    HiraganaConverterTheme {
        RequestReviewDialog(
            onLater = {},
            onOk = {},
        )
    }
}
