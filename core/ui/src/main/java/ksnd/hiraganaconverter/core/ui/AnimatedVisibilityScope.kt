package ksnd.hiraganaconverter.core.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.compositionLocalOf

// ref: https://docs.google.com/presentation/d/1kmKLeTsic2Fixxu63xFjVwu5wE1AC07HzGvD5mS9T0o/edit#slide=id.g2f7714da44f_0_728
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope> { error("Not initialize AnimatedVisibilityScope") }
