package ksnd.open.hiraganaconverter.view.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme

@Composable
fun TitleCard(text: String, painter: Painter) {
    Card(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = text,
                modifier = Modifier.padding(start = 8.dp).size(28.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 8.dp),
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTitleCard_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        TitleCard(text = "タイトル", painter = painterResource(id = R.drawable.ic_outline_info_24))
    }
}

@Preview
@Composable
private fun PreviewTitleCard_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        TitleCard(text = "タイトル", painter = painterResource(id = R.drawable.ic_outline_info_24))
    }
}