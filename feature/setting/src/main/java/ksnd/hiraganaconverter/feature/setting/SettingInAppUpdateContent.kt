package ksnd.hiraganaconverter.feature.setting

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard

@Composable
fun SettingInAppUpdateContent(
    enableInAppUpdate: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    TitleCard(
        text = stringResource(id = R.string.in_app_update_setting),
        painter = painterResource(id = R.drawable.baseline_system_update_24),
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.in_app_update_show_update),
                modifier = Modifier.padding(start = 16.dp).weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Switch(checked = enableInAppUpdate, onCheckedChange = onCheckedChange)
        }
    }
}
