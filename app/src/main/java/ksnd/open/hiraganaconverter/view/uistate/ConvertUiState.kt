package ksnd.open.hiraganaconverter.view.uistate

import ksnd.open.hiraganaconverter.model.HiraKanaType

data class ConvertUiState(
    val inputText: String = "",
    val outputText: String = "",
    val errorText: String = "",
    val selectedTextType: HiraKanaType = HiraKanaType.HIRAGANA
)
