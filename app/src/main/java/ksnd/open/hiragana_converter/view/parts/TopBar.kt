package ksnd.open.hiragana_converter.view.parts

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ksnd.open.hiragana_converter.view.dialog.InfoDialog
import ksnd.open.hiragana_converter.view.dialog.SettingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {

    val isShowSettingDialog = remember { mutableStateOf(false) }
    val isShowInfoDialog    = remember { mutableStateOf(false) }

    if(isShowSettingDialog.value) {
        SettingDialog(isShowDialog = isShowSettingDialog)
    }
    if(isShowInfoDialog.value) {
        InfoDialog(isShowInfoDialog = isShowInfoDialog)
    }

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            FilledTonalIconButton(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                onClick = { isShowInfoDialog.value = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "info",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            FilledTonalIconButton(
                onClick = { isShowSettingDialog.value = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "settings",
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