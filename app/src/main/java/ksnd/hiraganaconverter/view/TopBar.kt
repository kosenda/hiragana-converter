package ksnd.hiraganaconverter.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.parts.GooCreditImage
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    transitionHistory: () -> Unit,
    transitionSetting: () -> Unit,
    transitionInfo: () -> Unit,
) {
    val isShowTopBar by remember(scrollBehavior.state.collapsedFraction) {
        derivedStateOf { scrollBehavior.state.collapsedFraction != 1.toFloat() }
    }

    AnimatedVisibility(
        visible = isShowTopBar,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        TopAppBar(
            title = {},
            colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            actions = {
                CustomIconButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "info",
                    painter = painterResource(id = R.drawable.ic_outline_info_24),
                    onClick = transitionInfo,
                )
                CustomIconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    contentDescription = "settings",
                    painter = painterResource(id = R.drawable.ic_outline_settings_24),
                    onClick = transitionSetting,
                )
                CustomIconButton(
                    contentDescription = "history",
                    painter = painterResource(id = R.drawable.ic_baseline_history_24),
                    onClick = transitionHistory,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isTest().not()) {
                    GooCreditImage()
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@UiModePreview
@Composable
fun PreviewTopBar() {
    TopBar(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        transitionHistory = {},
        transitionSetting = {},
        transitionInfo = {},
    )
}
