package ksnd.hiraganaconverter.core.ui.parts.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModeAndLocalePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

private const val OfflineAnimationDuration = 500

@Composable
fun OfflineCard(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(OfflineAnimationDuration)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(OfflineAnimationDuration)),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text(
                text = stringResource(id = R.string.not_connected_network),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
            )
        }
    }
}

@UiModeAndLocalePreview
@Composable
private fun PreviewOfflineCard() {
    HiraganaConverterTheme {
        OfflineCard(visible = true)
    }
}
