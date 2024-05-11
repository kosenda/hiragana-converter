package ksnd.hiraganaconverter.core.ui.parts.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.contentBrush
import ksnd.hiraganaconverter.core.ui.theme.primaryBrush

@Composable
fun TitleCard(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(top = 28.dp, bottom = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .defaultMinSize(minHeight = 48.dp)
                .contentBrush(brush = primaryBrush()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = text,
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiaryContainer),
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewTitleCard() {
    HiraganaConverterTheme {
        TitleCard(text = "Title", painter = painterResource(id = R.drawable.ic_outline_info_24))
    }
}
