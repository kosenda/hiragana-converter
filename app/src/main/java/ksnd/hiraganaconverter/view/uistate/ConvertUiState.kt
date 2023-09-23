package ksnd.hiraganaconverter.view.uistate

import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.model.ConvertErrorType

data class ConvertUiState(
    val inputText: String = "",
    val outputText: String = "",
    val convertErrorType: ConvertErrorType? = null,
    val previousInputText: String = "",
    val selectedTextType: HiraKanaType = HiraKanaType.HIRAGANA,
    val isConverting: Boolean = false,
) {
    fun isChangedInputText(): Boolean {
        return inputText != "" && previousInputText != inputText
    }
}
