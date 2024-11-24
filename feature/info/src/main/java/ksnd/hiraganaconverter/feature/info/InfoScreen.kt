package ksnd.hiraganaconverter.feature.info

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
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
import ksnd.hiraganaconverter.core.ui.theme.CustomColor
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.LocalIsDarkTheme

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

            AppInfoContent(versionName = versionName)
            DeveloperInfoContent()
            APIInfoContent()
            LicensesContent(onClickLicense = onClickLicense)
            PrivacyPolicyContent()

            Spacer(modifier = Modifier.height(48.dp + navigationHeight))
        }
    }
}

@Composable
private fun AppInfoContent(versionName: String) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 16.dp)
        ) {
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
                DescItem(
                    title = R.string.app_name_title,
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                DescItem(
                    title = R.string.version_title,
                    text = versionName,
                )

                LinkText(
                    text = stringResource(id = R.string.google_play),
                    url = stringResource(id = R.string.review_url),
                    modifier = Modifier.padding(top = 16.dp),
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
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_play_icon),
                contentDescription = "convert",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(end = 16.dp)
                    .size(72.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                DescItem(
                    title = R.string.developer_name_title,
                    text = stringResource(R.string.developer_name),
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CustomIconButton(
                        painter = painterResource(id = R.drawable.ic_github_logo),
                        contentDescription = "",
                        modifier = Modifier.padding(end = 8.dp),
                        contentColor = Color.Black,
                        containerColor = Color.White,
                        onClick = { uriHandler.openUri(uri = "https://github.com/kosenda") },
                    )
                    CustomIconButton(
                        painter = painterResource(id = R.drawable.ic_x_logo),
                        contentDescription = "",
                        contentColor = Color.White,
                        containerColor = Color.Black,
                        onClick = { uriHandler.openUri(uri = "https://x.com/ksnd_dev") },
                    )
                }
            }
        }
    }
}

@Composable
private fun APIInfoContent() {
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
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
        ) {
            DescItem(
                title = R.string.api_name_title,
                text = stringResource(id = R.string.api_name),
                modifier = Modifier.padding(bottom = 4.dp),
            )

            LinkText(
                text = stringResource(id = R.string.url_title),
                url = stringResource(id = R.string.goo_url),
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (isTest().not()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                GooCreditImage()
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LinkText(
    text: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    val urlHandler = LocalUriHandler.current

    Row(
        modifier = modifier.clickable(
            onClick = {
                urlHandler.openUri(url)
            },
        ),
        verticalAlignment = Alignment.Bottom,
    ) {
        Image(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(20.dp),
            painter = painterResource(id = R.drawable.baseline_open_in_new_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = CustomColor.URL()),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = CustomColor.URL(),
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
private fun DescItem(
    @StringRes title: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, heightDp = 1100)
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, heightDp = 1100)
@Composable
fun PreviewInfoScreenContent() {
    CompositionLocalProvider(LocalIsDarkTheme provides isSystemInDarkTheme()) {
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
}
