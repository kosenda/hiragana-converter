package ksnd.hiraganaconverter.view.uistate

import ksnd.hiraganaconverter.model.ConvertErrorType
import ksnd.hiraganaconverter.model.HiraKanaType

data class ConvertUiState(
    val inputText: String = "",
    val outputText: String = "",
    val convertErrorType: ConvertErrorType? = null,
    val selectedTextType: HiraKanaType = HiraKanaType.HIRAGANA,
) {
    fun isChangedInputText(previousInputText: String): Boolean {
        return inputText != "" && previousInputText != inputText
    }
}
