package ksnd.hiraganaconverter.core.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    // Allow content to be displayed at the statusBar when scrolling
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val variableTopPaddingHeight by remember(scrollBehavior.state.collapsedFraction) {
        mutableStateOf(statusBarHeight * (1f - scrollBehavior.state.collapsedFraction))
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = variableTopPaddingHeight)
            .consumeWindowInsets(WindowInsets.statusBars),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(shape = CircleShape)
                    .size(48.dp)
                    .clickable(onClick = onBackPressed),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "back screen",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@UiModePreview
@Composable
fun PreviewBackTopBar() {
    HiraganaConverterTheme {
        BackTopBar(
            title = "Title",
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBackPressed = {},
        )
    }
}
