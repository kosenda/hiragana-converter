package ksnd.hiraganaconverter.feature.converter.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewFontScale
import ksnd.hiraganaconverter.core.ui.parts.card.ConvertHistoryCard
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@PreviewFontScale
@Composable
fun SamplePreviewFontScale() {
    HiraganaConverterTheme {
        ConvertHistoryCard(
            beforeText = "変換前文章",
            time = "2022/12/29 18:19",
            onClick = {},
            onDeleteClick = {},
        )
    }
}
