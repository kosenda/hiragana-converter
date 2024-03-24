package ksnd.hiraganaconverter.feature.info

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import ksnd.hiraganaconverter.core.analytics.LocalAnalytics
import ksnd.hiraganaconverter.core.analytics.Screen
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.isTest
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.GooCreditImage
import ksnd.hiraganaconverter.core.ui.parts.button.CustomIconButton
import ksnd.hiraganaconverter.core.ui.parts.button.TransitionButton
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard
import ksnd.hiraganaconverter.core.ui.parts.dialog.MovesToSiteDialog
import ksnd.hiraganaconverter.core.ui.preview.UiModePreview
import ksnd.hiraganaconverter.core.ui.theme.HiraganaConverterTheme
import ksnd.hiraganaconverter.core.ui.theme.urlColor

@Composable
fun InfoScreen(
    viewModel: InfoViewModel,
    onBackPressed: () -> Unit,
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logScreen(Screen.INFO)
    }

    InfoScreenContent(versionName = viewModel.versionName, onBackPressed = onBackPressed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoScreenContent(
    versionName: String,
    onBackPressed: () -> Unit,
) {
    val urlHandler = LocalUriHandler.current
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var isShowMovesToAppSiteDialog by remember { mutableStateOf(false) }
    var isShowMovesToApiSiteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface)
            .displayCutoutPadding(),
        topBar = {
            BackTopBar(scrollBehavior = scrollBehavior, onBackPressed = onBackPressed)
        },
    ) { padding ->
        if (isShowMovesToAppSiteDialog) {
            MovesToSiteDialog(
                onDismissRequest = {
                    isShowMovesToAppSiteDialog = false
                },
                onClick = {
                    isShowMovesToAppSiteDialog = false
                    urlHandler.openUri(uri = context.getString(R.string.review_url))
                },
                url = stringResource(id = R.string.review_url),
            )
        }
        if (isShowMovesToApiSiteDialog) {
            MovesToSiteDialog(
                onDismissRequest = {
                    isShowMovesToApiSiteDialog = false
                },
                onClick = {
                    isShowMovesToApiSiteDialog = false
                    urlHandler.openUri(uri = context.getString(R.string.goo_url))
                },
                url = stringResource(id = R.string.goo_url),
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        start = padding.calculateStartPadding(layoutDirection),
                        top = padding.calculateTopPadding(),
                        end = padding.calculateEndPadding(layoutDirection)
                    ),
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            AppInfoContent(versionName = versionName, onURLClick = { isShowMovesToAppSiteDialog = true })
            DeveloperInfoContent()
            APIInfoContent(onURLClick = { isShowMovesToApiSiteDialog = true })
            LicensesContent()
            PrivacyPolicyContent()
            Spacer(modifier = Modifier.height(40.dp))
        }
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
                        contentDescription = "",
                        painter = painterResource(id = R.drawable.ic_github_logo),
                        contentColor = null,
                        containerColor = Color.White,
                        onClick = { uriHandler.openUri(uri = "https://github.com/kosenda") },
                    )
                    CustomIconButton(
                        modifier = Modifier.padding(start = 8.dp),
                        contentDescription = "",
                        painter = painterResource(id = R.drawable.ic_x_logo),
                        contentColor = null,
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
private fun LicensesContent() {
    val context = LocalContext.current
    val buttonText = stringResource(id = R.string.oss_licenses)
    TitleCard(
        text = stringResource(id = R.string.licenses_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )
    TransitionButton(
        text = buttonText,
        onClick = {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)
            intent.putExtra("title", buttonText)
            ContextCompat.startActivity(context, intent, null)
        },
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
            )
        }
    }
}
