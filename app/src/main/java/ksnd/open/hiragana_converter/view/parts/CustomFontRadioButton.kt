package ksnd.open.hiragana_converter.view.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiragana_converter.model.CustomFont

/**
 * 設定画面で使用する選択フォントのラジオボタン
 */
@Composable
fun CustomFontRadioButton(
    onClick: () -> Unit,
    selected: Boolean,
    text: String,
    fontFamily: FontFamily?
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(40.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fontFamily
        )
        RadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(),
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun PreviewCustomFontRadioButton() {
    CustomFontRadioButton(
        onClick = {},
        selected = true,
        text = CustomFont.DEFAULT.name,
        fontFamily = FontFamily.Default
    )
}