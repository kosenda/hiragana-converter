package ksnd.hiraganaconverter.feature.info.licence.licensedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.m3.HtmlText
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDetailScreen(
    libraryName: String,
    licenseContent: String,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current.density
    var topBarHeight by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            BackTopBar(
                title = libraryName,
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .noRippleClickable {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                    .onSizeChanged { topBarHeight = it.height },
                onBackPressed = onBackPressed,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .consumeWindowInsets(innerPadding)
                .padding(
                    start = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.displayCutout
                        .asPaddingValues()
                        .calculateEndPadding(layoutDirection),
                )
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height((topBarHeight / density).toInt().dp))

            Text(
                text = libraryName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            HtmlText(
                html = licenseContent,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(48.dp + navigationBarHeight))
        }
    }
}

@UiModePreview
@Composable
fun PreviewLicenseDetailScreen() {
    HiraganaConverterTheme {
        LicenseDetailScreen(
            libraryName = "SampleLibrary",
            licenseContent = LoremIpsum().values.first(),
            onBackPressed = {},
        )
    }
}
