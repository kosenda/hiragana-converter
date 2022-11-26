package ksnd.open.hiraganaconverter.view.parts

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.dialog.ConvertHistoryDialog
import ksnd.open.hiraganaconverter.view.dialog.InfoDialog
import ksnd.open.hiraganaconverter.view.dialog.SettingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {
    var isShowSettingDialog by rememberSaveable { mutableStateOf(false) }
    var isShowInfoDialog by rememberSaveable { mutableStateOf(false) }
    var isShowConvertHistoryDialog by rememberSaveable { mutableStateOf(false) }

    if (isShowSettingDialog) {
        SettingDialog(
            onCloseClick = { isShowSettingDialog = false }
        )
    }
    if (isShowInfoDialog) {
        InfoDialog(
            onCloseClick = { isShowInfoDialog = false }
        )
    }
    if (isShowConvertHistoryDialog) {
        ConvertHistoryDialog(
            onCloseClick = { isShowConvertHistoryDialog = false }
        )
    }

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            FilledTonalIconButton(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                onClick = { isShowInfoDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "info",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            FilledTonalIconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { isShowSettingDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            FilledTonalIconButton(
                onClick = { isShowConvertHistoryDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_history_24),
                    contentDescription = "history",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://u.xgoo.jp/img/sgoo.png")
                    .crossfade(true)
                    .build(),
                contentDescription = "goo logo"
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTopBar() {
    TopBar(TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()))
}
