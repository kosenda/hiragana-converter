package ksnd.hiraganaconverter.feature.converter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType

abstract class ConvertViewModel : ViewModel() {
    abstract val uiState: StateFlow<ConvertUiState>
    abstract fun convert()
    abstract fun updateInputText(inputText: String)
    abstract fun updateOutputText(outputText: String)
    abstract fun clearConvertErrorType()
    abstract fun clearAllText()
    abstract fun changeHiraKanaType(type: HiraKanaType)
}
