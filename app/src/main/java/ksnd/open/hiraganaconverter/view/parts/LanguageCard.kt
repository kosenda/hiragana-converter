package ksnd.open.hiraganaconverter.view.parts

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.MainActivity

/**
 * 設定画面で使用する選択言語のカード
 */
@Composable
fun LanguageCard(
    onNewLanguageClick: (String) -> Unit,
    index: Int,
    displayLanguage: String
) {
    val context = LocalContext.current
    val languageList = stringArrayResource(id = R.array.language)
    OutlinedCard(
        modifier = Modifier
            .padding(all = 24.dp)
            .fillMaxWidth(0.7f)
            .height(96.dp)
            .clickable {
                val newLanguage = languageList[index]
                onNewLanguageClick(newLanguage)
                val intent = Intent(context, MainActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayLanguage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLanguageCard_Night() {
    val displayLanguageList = stringArrayResource(id = R.array.display_language)
    Column(Modifier.fillMaxWidth()) {
        LanguageCard(
            onNewLanguageClick = {},
            index = 0,
            displayLanguage = displayLanguageList[0]
        )
    }
}
