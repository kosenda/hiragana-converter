package ksnd.hiraganaconverter.feature.converter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.domain.NavKey
import ksnd.hiraganaconverter.core.domain.usecase.ConvertTextUseCase
import ksnd.hiraganaconverter.core.domain.usecase.toConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val convertTextUseCase: ConvertTextUseCase,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val receivedText = savedStateHandle.get<String>(NavKey.RECEIVED_TEXT) ?: ""

    private val _uiState = MutableStateFlow(ConvertUiState())
    val uiState: StateFlow<ConvertUiState> = _uiState.asStateFlow()

    init {
        if (receivedText.isNotEmpty()) _uiState.update { it.copy(inputText = receivedText) }
        savedStateHandle.remove<String>(NavKey.RECEIVED_TEXT)
    }

    fun convert() {
        // If input has not changed since the last time, it will not be converted.
        if (uiState.value.isChangedInputText().not()) return
        analytics.logConvert(hiraKanaType = uiState.value.selectedTextType.name, inputTextLength = uiState.value.inputText.length)
        viewModelScope.launch {
            _uiState.update { it.copy(isConverting = true) }
            runCatching {
                convertTextUseCase(uiState.value.inputText, uiState.value.selectedTextType)
            }.onSuccess { outputText ->
                _uiState.update {
                    it.copy(
                        outputText = outputText,
                        convertErrorType = null,
                        previousInputText = uiState.value.inputText,
                        isConverting = false,
                    )
                }
            }.onFailure { throwable ->
                val convertErrorType = throwable.toConvertErrorType()
                Timber.d("convert error type: $convertErrorType")
                analytics.logConvertError(error = convertErrorType.name)
                _uiState.update { it.copy(convertErrorType = convertErrorType, isConverting = false) }
            }
        }
    }

    fun updateInputText(inputText: String) {
        _uiState.update { it.copy(inputText = inputText) }
    }

    fun updateOutputText(outputText: String) {
        _uiState.update { it.copy(outputText = outputText) }
    }

    fun clearConvertErrorType() {
        _uiState.update { it.copy(convertErrorType = null) }
    }

    fun clearAllText() {
        if (uiState.value.inputText.isNotEmpty() || uiState.value.outputText.isNotEmpty()) analytics.logClearAllText()
        _uiState.update {
            it.copy(
                inputText = "",
                outputText = "",
                convertErrorType = null,
            )
        }
    }

    fun changeHiraKanaType(type: HiraKanaType) {
        analytics.logChangeHiraKanaType(hiraKanaType = type.name)
        _uiState.update {
            it.copy(
                selectedTextType = type,
                previousInputText = "",
            )
        }
    }
}
