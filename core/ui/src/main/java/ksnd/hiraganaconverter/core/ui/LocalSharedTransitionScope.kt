package ksnd.hiraganaconverter.core.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.staticCompositionLocalOf

// ref: https://docs.google.com/presentation/d/1kmKLeTsic2Fixxu63xFjVwu5wE1AC07HzGvD5mS9T0o/edit#slide=id.g2f7714da44f_0_739
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> { error("Not initialized SharedTransitionScope") }
