package ksnd.hiraganaconverter.view.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ksnd.hiraganaconverter.core.domain.NavKey

sealed class Nav {
    @Serializable
    data class Converter(
        @SerialName(NavKey.RECEIVED_TEXT)
        val receivedText: String = "",
    ) : Nav()

    @Serializable
    data object History : Nav()

    @Serializable
    data object Setting : Nav()

    @Serializable
    data object Info : Nav()
}
