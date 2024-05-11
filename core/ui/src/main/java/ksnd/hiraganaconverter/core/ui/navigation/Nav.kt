package ksnd.hiraganaconverter.core.ui.navigation

import kotlinx.serialization.Serializable

sealed class Nav {
    @Serializable
    data class Converter(val receivedText: String = "") : Nav()

    @Serializable
    data object History : Nav()

    @Serializable
    data object Setting : Nav()

    @Serializable
    data object Info : Nav()

    @Serializable
    data object License : Nav()
}
