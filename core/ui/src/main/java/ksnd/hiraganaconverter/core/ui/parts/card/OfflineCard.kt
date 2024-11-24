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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.LocalIsConnectNetwork
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

private const val OFFLINE_ANIMATION_DURATION = 500

@Composable
fun OfflineCard() {
    val isConnectNetwork = LocalIsConnectNetwork.current
    val isPreview = LocalInspectionMode.current

    AnimatedVisibility(
        visible = isConnectNetwork == false || isTest() || isPreview,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(OFFLINE_ANIMATION_DURATION)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(OFFLINE_ANIMATION_DURATION)),
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
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@UiModePreview
@Composable
fun PreviewOfflineCard() {
    HiraganaConverterTheme {
        OfflineCard()
    }
}
