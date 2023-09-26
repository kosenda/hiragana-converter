package ksnd.hiraganaconverter.feature.converter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType

class PreviewConvertViewModel : ConvertViewModel() {
    override val uiState: StateFlow<ConvertUiState> = MutableStateFlow(
        ConvertUiState(convertErrorType = ConvertErrorType.CONVERSION_FAILED),
    ).asStateFlow()
    override fun convert() {}
    override fun updateInputText(inputText: String) {}
    override fun updateOutputText(outputText: String) {}
    override fun clearConvertErrorType() {}
    override fun clearAllText() {}

    override fun changeHiraKanaType(type: HiraKanaType) {}
}
