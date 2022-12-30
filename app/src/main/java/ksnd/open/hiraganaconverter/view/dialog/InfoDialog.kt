package ksnd.open.hiraganaconverter.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ksnd.open.hiraganaconverter.BuildConfig
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.view.parts.button.BottomCloseButton
import ksnd.open.hiraganaconverter.view.parts.card.TitleCard
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.open.hiraganaconverter.view.theme.urlColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InfoDialog(onCloseClick: () -> Unit) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        InfoDialogContent(onCloseClick = onCloseClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoDialogContent(onCloseClick: () -> Unit) {
    val urlHandler = LocalUriHandler.current
    val context = LocalContext.current
    var isShowMovesToAppSiteDialog by remember { mutableStateOf(false) }
    var isShowMovesToApiSiteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(16.dp)),
        bottomBar = {
            BottomCloseButton(onClick = onCloseClick)
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
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            AppInfoContent(
                onURLClick = {
                    isShowMovesToAppSiteDialog = true
                },
            )

            DeveloperInfoContent()

            APIInfoContent(
                onURLClick = {
                    isShowMovesToApiSiteDialog = true
                },
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun AppInfoContent(onURLClick: () -> Unit) {
    TitleCard(
        text = stringResource(id = R.string.app_info_title),
        painter = painterResource(id = R.drawable.ic_outline_info_24),
    )
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
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
                BodyMedium(text = BuildConfig.VERSION_NAME)

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

@Composable
private fun DeveloperInfoContent() {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
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
                BodyMedium(text = stringResource(id = R.string.developer_name))
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
            .padding(all = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(stringResource(id = R.string.goo_credit_uri))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
            )
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

@Preview
@Composable
private fun PreviewInfoDialogContent_Light() {
    HiraganaConverterTheme(isDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            InfoDialogContent(onCloseClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewInfoDialogContent_Dark() {
    HiraganaConverterTheme(isDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            InfoDialogContent(onCloseClick = {})
        }
    }
}
