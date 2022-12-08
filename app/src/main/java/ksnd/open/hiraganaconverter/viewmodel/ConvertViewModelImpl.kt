package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.BuildConfig
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.model.ResponseData
import ksnd.open.hiraganaconverter.model.getNowTime
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertRepository
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.open.hiraganaconverter.view.ConvertUiState
import retrofit2.Response
import java.util.*
import javax.inject.Inject

const val LIMIT_CONVERT_COUNT = 200

@HiltViewModel
class ConvertViewModelImpl @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository
) : ConvertViewModel() {

    private val tag = ConvertViewModelImpl::class.java.simpleName

    private val _uiState = MutableStateFlow(ConvertUiState())
    override val uiState: StateFlow<ConvertUiState> = _uiState.asStateFlow()

    private val isReachedLimit = mutableStateOf(false)
    private val raw: MutableState<Response<ResponseData>?> = mutableStateOf(null)
    private val previousInputText: MutableState<String> = mutableStateOf("")

    private val oldLastConvertTimeFlow: StateFlow<String> = dataStoreRepository
        .lastConvertTime()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )
    private val oldConvertCountFlow: StateFlow<Int> = dataStoreRepository
        .convertCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 1
        )

    override fun convert(context: Context) {
        // 入力値なしまたは前回入力値のままであるときは後続処理を行わない
        if (uiState.value.inputText == "" ||
            previousInputText.value == uiState.value.inputText
        ) {
            return
        }

        // 非同期でリミットを確認する(日付が変わった時に対応できるようにするため)
        checkReachedLimit(context)
        if (isReachedLimit.value) {
            _uiState.update {
                it.copy(
                    errorText = context.getString(R.string.limit_local_count, LIMIT_CONVERT_COUNT)
                )
            }
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            // 変換
            raw.value = convertRepository.requestConvert(
                sentence = uiState.value.inputText,
                type = uiState.value.selectedTextType.name.lowercase(Locale.ENGLISH),
                appId = BuildConfig.apiKey
            )

            // 変換後の文字列を表示
            _uiState.update { it.copy(outputText = raw.value?.body()?.converted ?: "") }
            Log.i(tag, "outputText: ${uiState.value.outputText}")

            // 変換した文字列(input)を設定
            previousInputText.value = uiState.value.inputText
            Log.i(tag, "inputText: ${uiState.value.inputText}")

            // レスポンスがNullの場合はエラーメッセージを表示
            if (raw.value == null) {
                _uiState.update { it.copy(errorText = context.getString(R.string.conversion_failed)) }
            }

            raw.value?.let { response ->
                if (response.isSuccessful) {
                    // 変換が成功したときはエラーメッセージを消去し履歴を追加
                    _uiState.update { it.copy(errorText = "") }
                    insertConvertHistory(
                        beforeText = uiState.value.inputText,
                        afterText = uiState.value.outputText,
                        context = context
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

    private fun checkReachedLimit(context: Context) {
        val today = getNowTime(
            timeZone = context.getString(R.string.time_zone),
            format = "yyyy-MM-dd"
        )
        Log.i(tag, "old_convert_time: ${oldLastConvertTimeFlow.value}")
        Log.i(tag, "old_convert_count: ${oldConvertCountFlow.value}")
        isReachedLimit.value = if (today != oldLastConvertTimeFlow.value) {
            dataStoreRepository.updateLastConvertTime(today)
            dataStoreRepository.updateConvertCount(1)
            Log.i(tag, "new_convert_count: 1")
            Log.i(tag, "new_convert_time: $today")
            false
        } else {
            val newConvertCount = oldConvertCountFlow.value + 1
            Log.i(tag, "new_convert_count: $newConvertCount")
            dataStoreRepository.updateConvertCount(newConvertCount)
            newConvertCount > LIMIT_CONVERT_COUNT
        }
    }

    private fun insertConvertHistory(beforeText: String, afterText: String, context: Context) {
        val convertHistoryData = ConvertHistoryData(
            before = beforeText,
            after = afterText,
            time = getNowTime(
                timeZone = context.getString(R.string.time_zone),
                format = "yyyy/MM/dd HH:mm"
            )
        )
        CoroutineScope(Dispatchers.IO).launch {
            convertHistoryRepository.insertConvertHistory(convertHistoryData)
        }
    }
}
