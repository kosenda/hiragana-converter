package ksnd.hiraganaconverter.core.ui.parts

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ksnd.hiraganaconverter.core.resource.R

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
