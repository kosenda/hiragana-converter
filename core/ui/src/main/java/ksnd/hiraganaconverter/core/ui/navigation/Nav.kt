package ksnd.hiraganaconverter.core.ui.navigation

import kotlinx.serialization.Serializable
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

sealed class Nav {
    @Serializable
    data class Converter(val receivedText: String = "") : Nav()

    @Serializable
    data object History : Nav()

    @Serializable
    data class HistoryDetail(val historyData: ConvertHistoryData) : Nav()

    @Serializable
    data object Setting : Nav()

    @Serializable
    data object Info : Nav()

    @Serializable
    data object License : Nav()

    @Serializable
    data class LicenseDetail(
        val libraryName: String,
        val licenseContent: String,
    ) : Nav()
}
