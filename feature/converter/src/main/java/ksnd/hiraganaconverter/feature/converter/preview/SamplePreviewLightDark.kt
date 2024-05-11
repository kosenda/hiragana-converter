package ksnd.hiraganaconverter.feature.converter.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ksnd.hiraganaconverter.core.ui.parts.dialog.DialogTopBar
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@PreviewLightDark
@Composable
fun SamplePreviewLightDark() {
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
