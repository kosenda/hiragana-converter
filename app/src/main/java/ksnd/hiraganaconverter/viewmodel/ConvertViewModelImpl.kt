package ksnd.hiraganaconverter.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ksnd.hiraganaconverter.BuildConfig
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.di.module.DefaultDispatcher
import ksnd.hiraganaconverter.di.module.IODispatcher
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.model.TimeFormat
import ksnd.hiraganaconverter.model.getNowTime
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.model.repository.ConvertRepository
import ksnd.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.hiraganaconverter.model.repository.LIMIT_CONVERT_COUNT
import ksnd.hiraganaconverter.view.uistate.ConvertUiState
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ConvertViewModelImpl @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ConvertViewModel() {

    private val _uiState = MutableStateFlow(ConvertUiState())
    override val uiState: StateFlow<ConvertUiState> = _uiState.asStateFlow()

    private val previousInputText: MutableState<String> = mutableStateOf("")

    override fun convert(context: Context) {
        if (uiState.value.isChangedInputText(previousInputText = previousInputText.value).not()) {
            return
        }

        CoroutineScope(ioDispatcher).launch {
            val isReachedConvertMaxLimit = withContext(defaultDispatcher) {
                dataStoreRepository.checkReachedConvertMaxLimit(
                    today = getNowTime(
                        timeZone = context.getString(R.string.time_zone),
                        format = TimeFormat.YEAR_MONTH_DATE,
                    ),
                )
            }

            if (isReachedConvertMaxLimit) {
                _uiState.update {
                    it.copy(
                        errorText = context.getString(
                            R.string.limit_local_count,
                            LIMIT_CONVERT_COUNT,
                        ),
                    )
                }
                return@launch
            }

            val response = convertRepository.requestConvert(
                sentence = uiState.value.inputText,
                type = uiState.value.selectedTextType.name.lowercase(Locale.ENGLISH),
                appId = BuildConfig.apiKey,
            )

            // 変換後の文字列を表示
            _uiState.update { it.copy(outputText = response?.body()?.converted ?: "") }
            Timber.i("outputText: %s", uiState.value.outputText)

            // 変換した文字列を記録
            previousInputText.value = uiState.value.inputText
            Timber.i("previousInputText: %s", previousInputText.value)

            if (response == null) {
                _uiState.update { it.copy(errorText = context.getString(R.string.conversion_failed)) }
            } else {
                if (response.isSuccessful) {
                    // 変換が成功したときはエラーメッセージを消去し履歴を追加
                    _uiState.update { it.copy(errorText = "") }
                    convertHistoryRepository.insertConvertHistory(
                        beforeText = uiState.value.inputText,
                        afterText = uiState.value.outputText,
                        time = getNowTime(
                            timeZone = context.getString(R.string.time_zone),
                            format = TimeFormat.YEAR_MONTH_DATE_HOUR_MINUTE,
                        ),
                    )
                } else {
                    // 変換が失敗したときはレスポンスのメッセージ（ErrorInterceptorで変換済み）を表示
                    _uiState.update { it.copy(errorText = response.message()) }
                }
            }

            Timber.i("errorText: %s", uiState.value.errorText)
        }
    }

    override fun updateInputText(inputText: String) {
        _uiState.update { it.copy(inputText = inputText) }
    }

    override fun updateOutputText(outputText: String) {
        _uiState.update { it.copy(outputText = outputText) }
    }

    override fun clearErrorText() {
        _uiState.update { it.copy(errorText = "") }
    }

    override fun clearAllText() {
        _uiState.update {
            it.copy(
                inputText = "",
                outputText = "",
                errorText = "",
            )
        }
    }

    override fun changeHiraKanaType(type: HiraKanaType) {
        _uiState.update { it.copy(selectedTextType = type) }
        previousInputText.value = ""
    }
}
