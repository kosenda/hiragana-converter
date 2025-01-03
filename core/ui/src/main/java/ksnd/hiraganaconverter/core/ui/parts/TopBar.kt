package ksnd.hiraganaconverter.core.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigateScreen: (Nav) -> Unit,
) {
    // Allow content to be displayed at the statusBar when scrolling
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val variableTopPaddingHeight by remember(scrollBehavior.state.collapsedFraction) {
        mutableStateOf(statusBarHeight * (1f - scrollBehavior.state.collapsedFraction))
    }

    TopAppBar(
        title = {},
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = variableTopPaddingHeight)
            .consumeWindowInsets(WindowInsets.statusBars),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            CustomIconButton(
                icon = R.drawable.ic_outline_info_24,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = dropUnlessResumed { navigateScreen(Nav.Info) },
            )
            CustomIconButton(
                icon = R.drawable.ic_outline_settings_24,
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp),
                onClick = dropUnlessResumed { navigateScreen(Nav.Setting) },
            )
            CustomIconButton(
                icon = R.drawable.ic_baseline_history_24,
                contentDescription = "",
                onClick = dropUnlessResumed { navigateScreen(Nav.History) },
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isTest().not()) {
                GooCreditImage()
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@UiModePreview
@Composable
fun PreviewTopBar() {
    HiraganaConverterTheme {
        TopBar(
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
            navigateScreen = {},
        )
    }
}
