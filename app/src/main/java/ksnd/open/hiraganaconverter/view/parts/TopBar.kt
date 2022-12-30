package ksnd.open.hiraganaconverter.view.parts

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.dialog.ConvertHistoryDialog
import ksnd.open.hiraganaconverter.view.dialog.InfoDialog
import ksnd.open.hiraganaconverter.view.dialog.SettingDialog
import ksnd.open.hiraganaconverter.view.parts.button.CustomFilledTonalIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {
    var isShowSettingDialog by rememberSaveable { mutableStateOf(false) }
    var isShowInfoDialog by rememberSaveable { mutableStateOf(false) }
    var isShowConvertHistoryDialog by rememberSaveable { mutableStateOf(false) }

    if (isShowSettingDialog) {
        SettingDialog(
            onCloseClick = { isShowSettingDialog = false },
        )
    }
    if (isShowInfoDialog) {
        InfoDialog(
            onCloseClick = { isShowInfoDialog = false },
        )
    }
    if (isShowConvertHistoryDialog) {
        ConvertHistoryDialog(
            onCloseClick = { isShowConvertHistoryDialog = false },
        )
    }

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            CustomFilledTonalIconButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                contentDescription = "info",
                painter = painterResource(id = R.drawable.ic_outline_info_24),
                onClick = { isShowInfoDialog = true },
            )
            CustomFilledTonalIconButton(
                modifier = Modifier.padding(end = 16.dp),
                contentDescription = "settings",
                painter = painterResource(id = R.drawable.ic_outline_settings_24),
                onClick = { isShowSettingDialog = true },
            )
            CustomFilledTonalIconButton(
                contentDescription = "history",
                painter = painterResource(id = R.drawable.ic_baseline_history_24),
                onClick = { isShowConvertHistoryDialog = true },
            )
            Spacer(modifier = Modifier.weight(1f))
            GooCreditImage()
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTopBar() {
    TopBar(TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()))
}
