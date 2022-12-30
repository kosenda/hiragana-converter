package ksnd.open.hiraganaconverter.view.parts

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ksnd.open.hiraganaconverter.R

@Composable
fun GooCreditImage() {
    AsyncImage(
        ImageRequest.Builder(LocalContext.current)
            .data(stringResource(id = R.string.goo_credit_uri))
            .crossfade(true)
            .build(),
        contentDescription = "goo credit",
    )
}
