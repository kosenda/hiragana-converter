package ksnd.hiraganaconverter.core.ui.preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import ksnd.hiraganaconverter.core.ui.LocalAnimatedVisibilityScope
import ksnd.hiraganaconverter.core.ui.LocalSharedTransitionScope

// ref: https://stackoverflow.com/questions/78656716/jetpack-compose-preview-for-screen-that-have-sharedtransitionscope-animatedvisi
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionAndAnimatedVisibilityProvider(content: @Composable () -> Unit) {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout,
                LocalAnimatedVisibilityScope provides this@AnimatedVisibility,
            ) {
                content()
            }
        }
    }
}
