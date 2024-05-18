package ksnd.hiraganaconverter.core.ui.parts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigateScreen: (Nav) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val isShowTopBar by remember(scrollBehavior.state.collapsedFraction) {
        derivedStateOf { scrollBehavior.state.collapsedFraction != 1.toFloat() }
    }

    AnimatedVisibility(
        visible = isShowTopBar,
        modifier = modifier.padding(
            start = WindowInsets.displayCutout
                .asPaddingValues()
                .calculateStartPadding(layoutDirection),
            end = WindowInsets.displayCutout
                .asPaddingValues()
                .calculateEndPadding(layoutDirection),
        ),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        TopAppBar(
            title = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            actions = {
                CustomIconButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "info",
                    painter = painterResource(id = R.drawable.ic_outline_info_24),
                    onClick = dropUnlessResumed { navigateScreen(Nav.Info) },
                )
                CustomIconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    contentDescription = "settings",
                    painter = painterResource(id = R.drawable.ic_outline_settings_24),
                    onClick = dropUnlessResumed { navigateScreen(Nav.Setting) },
                )
                CustomIconButton(
                    contentDescription = "history",
                    painter = painterResource(id = R.drawable.ic_baseline_history_24),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@UiModePreview
@Composable
fun PreviewTopBar() {
    TopBar(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        navigateScreen = {},
    )
}
