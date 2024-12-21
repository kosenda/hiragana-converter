package ksnd.hiraganaconverter.feature.converter

import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType

data class ConvertUiState(
    val inputText: String = "",
    val outputText: String = "",
    val convertErrorType: ConvertErrorType? = null,
    val previousInputText: String = "",
    val selectedTextType: HiraKanaType = HiraKanaType.HIRAGANA,
    val isConverting: Boolean = false,
    val showRequestReview: Boolean = false,
    val showErrorCard: Boolean = false,
) {
    fun isChangedInputText(): Boolean = inputText != "" && previousInputText != inputText
}
