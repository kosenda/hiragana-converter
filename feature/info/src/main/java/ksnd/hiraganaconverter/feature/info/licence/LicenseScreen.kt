package ksnd.hiraganaconverter.feature.info.licence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.util.author
import com.mikepenz.aboutlibraries.ui.compose.m3.util.htmlReadyLicenseContent
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.rememberButtonScaleState
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.feature.info.licence.mock.MockLibs

@Composable
fun LicenseScreen(
    viewModel: LicenseViewModel,
    navigateLicenseDetail: (libraryName: String, licenseContent: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val libs by viewModel.libs.collectAsStateWithLifecycle()

    LicenseContent(
        libs = libs,
        navigateLicenseDetail = navigateLicenseDetail,
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseContent(
    libs: Libs?,
    navigateLicenseDetail: (libraryName: String, licenseContent: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface)
            .displayCutoutPadding(),
        topBar = {
            BackTopBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.noRippleClickable {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                onBackPressed = onBackPressed,
                title = {
                    Text(
                        text = stringResource(id = R.string.licenses_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(
                paddingValues = PaddingValues(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                ),
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(
                items = libs?.libraries ?: emptyList(),
                key = { it.uniqueId },
            ) { library ->
                LibraryItem(
                    library = library,
                    navigateLicenseDetail = navigateLicenseDetail,
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(48.dp),
                )
            }
        }
    }
}

@Composable
fun LibraryItem(
    library: Library,
    navigateLicenseDetail: (libraryName: String, licenseContent: String) -> Unit,
) {
    val urlHandler = LocalUriHandler.current
    val buttonScaleState = rememberButtonScaleState()
    val isNotExistLicenseContent = library.licenses.firstOrNull()?.htmlReadyLicenseContent.isNullOrBlank()

    Card(
        modifier = Modifier
            .scale(scale = buttonScaleState.animationScale.value)
            .noRippleClickable(
                interactionSource = buttonScaleState.interactionSource,
                onClick = {
                    if (isNotExistLicenseContent) {
                        library.licenses.first().url?.let {
                            urlHandler.openUri(it)
                        }
                    } else {
                        navigateLicenseDetail(library.name, library.licenses.first().htmlReadyLicenseContent!!)
                    }
                },
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = library.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )

                if (library.author.isNotBlank()) {
                    Text(
                        text = library.author,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }

                library.artifactVersion?.let { version ->
                    Text(
                        text = "version: %s".format(version),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                library.licenses.forEach { license ->
                    Badge(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                    ) {
                        Text(
                            text = license.name,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            if (isNotExistLicenseContent) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                    contentDescription = "open license",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}

@UiModePreview
@Composable
fun PreviewLicenseContent() {
    HiraganaConverterTheme {
        LicenseContent(
            libs = MockLibs.item,
            navigateLicenseDetail = { _, _ -> },
            onBackPressed = {},
        )
    }
}
