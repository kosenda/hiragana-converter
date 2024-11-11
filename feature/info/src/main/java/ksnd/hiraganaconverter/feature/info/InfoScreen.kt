package ksnd.hiraganaconverter.feature.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.GooCreditImage
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.parts.dialog.MoveToBrowserDialog
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.urlColor

@Composable
fun InfoScreen(
    viewModel: InfoViewModel,
    onBackPressed: () -> Unit,
    onClickLicense: () -> Unit,
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.INFO)
    }

    InfoScreenContent(
        versionName = viewModel.versionName,
        onBackPressed = onBackPressed,
        onClickLicense = onClickLicense,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoScreenContent(
    versionName: String,
    onBackPressed: () -> Unit,
    onClickLicense: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var isShowMovesToAppSiteDialog by remember { mutableStateOf(false) }
    var isShowMovesToApiSiteDialog by remember { mutableStateOf(false) }
    var topBarHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current.density
    val navigationHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.title_info),
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
                .fillMaxSize()
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
            AppInfoContent(versionName = versionName, onURLClick = { isShowMovesToAppSiteDialog = true })
            DeveloperInfoContent()
            APIInfoContent(onURLClick = { isShowMovesToApiSiteDialog = true })
            LicensesContent(onClickLicense = onClickLicense)
            PrivacyPolicyContent()
            Spacer(modifier = Modifier.height(48.dp + navigationHeight))
        }
    }

    if (isShowMovesToAppSiteDialog) {
        MoveToBrowserDialog(
            onDismissRequest = {
                isShowMovesToAppSiteDialog = false
            },
            onMoveToBrowser = {
                isShowMovesToAppSiteDialog = false
            },
            url = stringResource(id = R.string.review_url),
        )
    }

    if (isShowMovesToApiSiteDialog) {
        MoveToBrowserDialog(
            onDismissRequest = {
                isShowMovesToApiSiteDialog = false
            },
            onMoveToBrowser = {
                isShowMovesToApiSiteDialog = false
            },
            url = stringResource(id = R.string.goo_url),
        )
    }
}

@Composable
private fun AppInfoContent(versionName: String, onURLClick: () -> Unit) {
    TitleCard(
        text = stringResource(id = R.string.app_info_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "convert",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .size(72.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                ItemTitle(
                    text = stringResource(id = R.string.app_name_title),
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                BodyMedium(text = stringResource(id = R.string.app_name))

                ItemTitle(
                    text = stringResource(id = R.string.version_title),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp),
                )
                BodyMedium(text = versionName)

                ItemTitle(
                    text = stringResource(id = R.string.google_play),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp),
                )
                UrlText(
                    url = stringResource(id = R.string.review_url),
                    onURLClick = onURLClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DeveloperInfoContent() {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.google_play_icon),
                contentDescription = "convert",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .size(72.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                ItemTitle(
                    text = stringResource(id = R.string.developer_name_title),
                    modifier = Modifier.padding(bottom = 4.dp),
                )

                FlowRow {
                    BodyMedium(text = stringResource(id = R.string.developer_name))
                    CustomIconButton(
                        painter = painterResource(id = R.drawable.ic_github_logo),
                        contentDescription = "",
                        contentColor = Color.Black,
                        containerColor = Color.White,
                        onClick = { uriHandler.openUri(uri = "https://github.com/kosenda") },
                    )
                    CustomIconButton(
                        painter = painterResource(id = R.drawable.ic_x_logo),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 8.dp),
                        contentColor = Color.White,
                        containerColor = Color.Black,
                        onClick = { uriHandler.openUri(uri = "https://twitter.com/ksnd_dev") },
                    )
                }
            }
        }
    }
}

@Composable
private fun APIInfoContent(onURLClick: () -> Unit) {
    TitleCard(
        text = stringResource(id = R.string.api_info_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        if (isTest().not()) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                GooCreditImage()
            }
        }
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            ItemTitle(
                text = stringResource(id = R.string.api_name_title),
                modifier = Modifier.padding(bottom = 4.dp),
            )
            BodyMedium(text = stringResource(id = R.string.api_name))

            ItemTitle(
                text = stringResource(id = R.string.url_title),
                modifier = Modifier.padding(bottom = 4.dp, top = 16.dp),
            )
            UrlText(
                url = stringResource(id = R.string.goo_url),
                onURLClick = onURLClick,
            )
        }
    }
}

@Composable
private fun LicensesContent(onClickLicense: () -> Unit) {
    val buttonText = stringResource(id = R.string.oss_licenses)

    TitleCard(
        text = stringResource(id = R.string.licenses_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )
    TransitionButton(
        text = buttonText,
        onClick = onClickLicense,
    )
}

@Composable
private fun ItemTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(horizontal = 16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun BodyMedium(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier.padding(horizontal = 16.dp),
        textAlign = TextAlign.Left,
    )
}

@Composable
private fun UrlText(url: String, onURLClick: () -> Unit) {
    Text(
        text = url,
        style = MaterialTheme.typography.bodyLarge,
        color = urlColor,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onURLClick),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textDecoration = TextDecoration.Underline,
    )
}

@UiModePreview
@Composable
fun PreviewInfoScreenContent() {
    HiraganaConverterTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            InfoScreenContent(
                versionName = "1.0.0",
                onBackPressed = {},
                onClickLicense = {},
            )
        }
    }
}
