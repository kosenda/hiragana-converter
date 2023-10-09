package ksnd.hiraganaconverter.feature.converter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.domain.NavKey
import ksnd.hiraganaconverter.core.domain.usecase.ConversionFailedException
import ksnd.hiraganaconverter.core.domain.usecase.ConvertTextUseCase
import ksnd.hiraganaconverter.core.domain.usecase.InterceptorError
import ksnd.hiraganaconverter.core.domain.usecase.IsReachedConvertMaxLimitException
import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertViewModelImpl @Inject constructor(
    private val convertTextUseCase: ConvertTextUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ConvertViewModel() {
    private val receivedText = savedStateHandle.get<String>(NavKey.RECEIVED_TEXT) ?: ""

    private val _uiState = MutableStateFlow(ConvertUiState())
    override val uiState: StateFlow<ConvertUiState> = _uiState.asStateFlow()

    init {
        if (receivedText.isNotEmpty()) _uiState.update { it.copy(inputText = receivedText) }
        savedStateHandle.remove<String>(NavKey.RECEIVED_TEXT)
    }

    override fun convert() {
        // If input has not changed since the last time, it will not be converted.
        if (uiState.value.isChangedInputText().not()) return

        CoroutineScope(ioDispatcher).launch {
            _uiState.update { it.copy(isConverting = true) }
            runCatching {
                convertTextUseCase(uiState.value.inputText, uiState.value.selectedTextType)
            }.onSuccess { outputText ->
                _uiState.update {
                    it.copy(
                        outputText = outputText,
                        convertErrorType = null,
                        previousInputText = uiState.value.inputText,
                    )
                }
            }.onFailure { throwable ->
                Timber.d("convert throwable: $throwable")
                _uiState.update {
                    it.copy(
                        convertErrorType = when (throwable) {
                            is IsReachedConvertMaxLimitException -> ConvertErrorType.REACHED_CONVERT_MAX_LIMIT
                            is ConversionFailedException -> ConvertErrorType.CONVERSION_FAILED
                            is InterceptorError -> when (throwable.message) {
                                ConvertErrorType.TOO_MANY_CHARACTER.name -> ConvertErrorType.TOO_MANY_CHARACTER
                                ConvertErrorType.RATE_LIMIT_EXCEEDED.name -> ConvertErrorType.RATE_LIMIT_EXCEEDED
                                ConvertErrorType.CONVERSION_FAILED.name -> ConvertErrorType.CONVERSION_FAILED
                                ConvertErrorType.INTERNAL_SERVER.name -> ConvertErrorType.INTERNAL_SERVER
                                ConvertErrorType.NETWORK.name -> ConvertErrorType.NETWORK
                                else -> {
                                    Timber.w("InterceptorError: %s".format(throwable.message))
                                    ConvertErrorType.CONVERSION_FAILED
                                }
                            }
                            else -> throw IllegalStateException("Not defined ConvertTextUseCaseException!")
                        },
                    )
                }
            }
            _uiState.update { it.copy(isConverting = false) }
        }
    }

    override fun updateInputText(inputText: String) {
        _uiState.update { it.copy(inputText = inputText) }
    }

    override fun updateOutputText(outputText: String) {
        _uiState.update { it.copy(outputText = outputText) }
    }

    override fun clearConvertErrorType() {
        _uiState.update { it.copy(convertErrorType = null) }
    }

    override fun clearAllText() {
        _uiState.update {
            it.copy(
                inputText = "",
                outputText = "",
                convertErrorType = null,
            )
        }
    }

    override fun changeHiraKanaType(type: HiraKanaType) {
        _uiState.update {
            it.copy(
                selectedTextType = type,
                previousInputText = "",
            )
        }
    }
}
