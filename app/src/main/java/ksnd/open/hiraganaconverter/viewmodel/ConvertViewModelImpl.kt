package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import android.util.Log
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
import ksnd.open.hiraganaconverter.BuildConfig
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.di.module.DefaultDispatcher
import ksnd.open.hiraganaconverter.di.module.IODispatcher
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.model.TimeFormat
import ksnd.open.hiraganaconverter.model.getNowTime
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertRepository
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.open.hiraganaconverter.model.repository.LIMIT_CONVERT_COUNT
import ksnd.open.hiraganaconverter.view.uistate.ConvertUiState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConvertViewModelImpl @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ConvertViewModel() {

    private val tag = ConvertViewModelImpl::class.java.simpleName

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
                        format = TimeFormat.YEAR_MONTH_DATE
                    )
                )
            }

            if (isReachedConvertMaxLimit) {
                _uiState.update {
                    it.copy(
                        errorText = context.getString(R.string.limit_local_count, LIMIT_CONVERT_COUNT)
                    )
                }
                return@launch
            }

            val response = convertRepository.requestConvert(
                sentence = uiState.value.inputText,
                type = uiState.value.selectedTextType.name.lowercase(Locale.ENGLISH),
                appId = BuildConfig.apiKey
            )

            // 変換後の文字列を表示
            _uiState.update { it.copy(outputText = response?.body()?.converted ?: "") }
            Log.i(tag, "outputText: ${uiState.value.outputText}")

            // 変換した文字列を記録
            previousInputText.value = uiState.value.inputText
            Log.i(tag, "previousInputText: ${previousInputText.value}")

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
                            format = TimeFormat.YEAR_MONTH_DATE_HOUR_MINUTE
                        )
                    )
                } else {
                    // 変換が失敗したときはレスポンスのメッセージ（ErrorInterceptorで変換済み）を表示
                    _uiState.update { it.copy(errorText = response.message()) }
                }
            }

            Log.i(tag, "errorText: ${uiState.value.errorText}")
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

    override fun changeHiraKanaType(type: HiraKanaType) {
        _uiState.update { it.copy(selectedTextType = type) }
        previousInputText.value = ""
    }
}
