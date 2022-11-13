package ksnd.hiraganaconverter.view.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TitleCard(text: String) {
    Card(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(32.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = 8.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTitleCard() {
    TitleCard(text = "タイトル")
}