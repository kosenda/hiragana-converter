package ksnd.open.hiragana_converter.view.dialog

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import ksnd.hiraganaconverter.view.parts.TitleCard
import ksnd.open.hiragana_converter.view.parts.BottomCloseButton
import ksnd.open.hiragana_converter.BuildConfig
import ksnd.open.hiragana_converter.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InfoDialog(isShowInfoDialog: MutableState<Boolean>) {
    
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        InfoDialogContent(isShowInfoDialog)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoDialogContent(isShowInfoDialog: MutableState<Boolean>) {

    val urlHandler = LocalUriHandler.current
    val context = LocalContext.current
    val urlColor = Color(0xFF00D4AA)
    var isShowMovesToAppSiteDialog by remember { mutableStateOf(false) }
    var isShowMovesToApiSiteDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(16.dp)),
        bottomBar = {
            BottomCloseButton(onClick = { isShowInfoDialog.value = false } )
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
                url = stringResource(id = R.string.review_url)
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
                url = stringResource(id = R.string.goo_url)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // アプリの情報
            TitleCard(text = stringResource(id = R.string.app_info_title))
            Card(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
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
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        ItemTitle(
                            text = stringResource(id = R.string.app_name_title),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        BodyMedium(text = stringResource(id = R.string.app_name))

                        ItemTitle(
                            text = stringResource(id = R.string.version_title),
                            modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                        )
                        BodyMedium(text = BuildConfig.VERSION_NAME)

                        ItemTitle(
                            text = stringResource(id = R.string.google_play),
                            modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.review_url),
                            style = MaterialTheme.typography.bodyLarge,
                            color = urlColor,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    isShowMovesToAppSiteDialog = true
                                },
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }

            // 開発者情報
            Card(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
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
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        ItemTitle(
                            text = stringResource(id = R.string.developer_name_title),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        BodyMedium(text = stringResource(id = R.string.developer_name))
                    }
                }
            }

            // APIの情報
            TitleCard(text = stringResource(id = R.string.api_info_title))
            Card(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    AsyncImage(
                        ImageRequest.Builder(LocalContext.current)
                            .data("https://u.xgoo.jp/img/sgoo.png")
                            .crossfade(true)
                            .build(),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    ItemTitle(
                        text = stringResource(id = R.string.api_name_title),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    BodyMedium(text = stringResource(id = R.string.api_name))

                    ItemTitle(
                        text = stringResource(id = R.string.url_title),
                        modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.goo_url),
                        style = MaterialTheme.typography.bodyLarge,
                        color = urlColor,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                isShowMovesToApiSiteDialog = true
                            },
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
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

@Preview
@Composable
private fun PreviewInfoDialogContent() {
    InfoDialogContent(isShowInfoDialog = mutableStateOf(true))
}

