package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.view.ConvertUiState

abstract class ConvertViewModel : ViewModel() {
    abstract val uiState: StateFlow<ConvertUiState>
    abstract fun convert(context: Context)
    abstract fun updateInputText(inputText: String)
    abstract fun updateOutputText(outputText: String)
    abstract fun clearErrorText()
    abstract fun changeHiraKanaType(type: HiraKanaType)
}
