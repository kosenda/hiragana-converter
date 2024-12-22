package ksnd.hiraganaconverter.core.ui.extension

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ksnd.hiraganaconverter.core.ui.LocalAnimatedVisibilityScope
import ksnd.hiraganaconverter.core.ui.LocalSharedTransitionScope

// ref: https://docs.google.com/presentation/d/1kmKLeTsic2Fixxu63xFjVwu5wE1AC07HzGvD5mS9T0o/edit#slide=id.g2f7714da44f_0_749
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.easySharedBounds(key: Any): Modifier {
    with(LocalSharedTransitionScope.current) {
        return this@easySharedBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
        )
    }
}
