package ksnd.hiraganaconverter.view.uistate

import ksnd.hiraganaconverter.model.HiraKanaType

data class ConvertUiState(
    val inputText: String = "",
    val outputText: String = "",
    val errorText: String = "",
    val selectedTextType: HiraKanaType = HiraKanaType.HIRAGANA,
) {
    fun isChangedInputText(previousInputText: String): Boolean {
        return inputText != "" && previousInputText != inputText
    }
}
