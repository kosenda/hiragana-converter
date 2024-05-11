package ksnd.hiraganaconverter.feature.info.licence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.R
import ksnd.hiraganaconverter.core.ui.extension.noRippleClickable
import ksnd.hiraganaconverter.core.ui.parts.BackTopBar
import ksnd.hiraganaconverter.core.ui.parts.card.TitleCard

@Composable
fun LicenseScreen(viewModel: LicenseViewModel, onBackPressed: () -> Unit) {
    LicenseContent(onBackPressed = onBackPressed)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LicenseContent(
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
            )
        },
    ) { innerPadding ->
        LibrariesContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            lazyListState = lazyListState,
            header = {
                item {
                    TitleCard(
                        text = stringResource(id = R.string.licenses_title),
                        painter = painterResource(id = R.drawable.ic_outline_info_24),
                        modifier = Modifier.padding(start = 16.dp),
                    )
                }
            }
        )
    }
}
