package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.BuildConfig
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.model.ResponseData
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertRepository
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConvertViewModelImpl @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository
) : ConvertViewModel() {

    private val tag = ConvertViewModelImpl::class.java.simpleName
    private val limitConvertCount = 200
    private val isReachedLimit = mutableStateOf(false)

    override val previousInputText: MutableState<String> = mutableStateOf("")
    override val inputText: MutableState<String> = mutableStateOf("")
    override val outputText: MutableState<String> = mutableStateOf("")
    override val errorText: MutableState<String> = mutableStateOf("")
    override val selectedTextType: MutableState<HiraKanaType> =
        mutableStateOf(HiraKanaType.HIRAGANA)
    override val raw: MutableState<Response<ResponseData>?> = mutableStateOf(null)

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
        if (inputText.value == "" || previousInputText.value == inputText.value) {
            return
        }
        if (isReachedLimit.value) {
            errorText.value = context.getString(R.string.limit_local_count, limitConvertCount)
            return
        }
        val appId = BuildConfig.apiKey
        val nullRawErrorText = context.getString(R.string.conversion_failed)
        CoroutineScope(Dispatchers.IO).launch {
            raw.value = convertRepository.requestConvert(
                sentence = inputText.value,
                type = selectedTextType.value.name.lowercase(Locale.ENGLISH),
                appId = appId
            )
            if (raw.value == null) {
                errorText.value = nullRawErrorText
            }
            outputText.value = raw.value?.body()?.converted ?: ""
            previousInputText.value = inputText.value
            insertConvertHistory(
                beforeText = inputText.value,
                afterText = outputText.value,
                context = context
            )
            checkReachedLimit()
        }
    }

    override fun updateErrorText(context: Context) {
        Log.i(tag, "raw: ${raw.value?.raw()}")
        when (raw.value?.code()) {
            null -> {
                errorText.value = "" // 初期化時しか通らない
            }
            200 -> {
                errorText.value = ""
            }
            413 -> {
                errorText.value = context.getString(R.string.request_too_large)
            }
            400 -> {
                errorText.value = when (raw.value?.message()) {
                    "Rate limit exceeded" -> {
                        context.getString(R.string.limit_exceeded)
                    }
                    else -> {
                        context.getString(R.string.conversion_failed)
                    }
                }
            }
            else -> {
                errorText.value = context.getString(R.string.conversion_failed)
            }
        }
        Log.i(tag, "errorText: ${errorText.value}")
    }

    /**
     * 制限回数に達していることを確認する処理
     */
    private fun checkReachedLimit() {
        val today = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
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
            newConvertCount > limitConvertCount
        }
    }

    override fun insertConvertHistory(beforeText: String, afterText: String, context: Context) {
        val convertHistoryData = ConvertHistoryData(
            before = beforeText,
            after = afterText,
            time = DateFormat.format(
                "yyyy/MM/dd HH:mm",
                Calendar.getInstance(
                    if ("default" == context.getString(R.string.time_zone)) {
                        TimeZone.getDefault()
                    } else {
                        TimeZone.getTimeZone(context.getString(R.string.time_zone))
                    }
                )
            ).toString()
        )
        CoroutineScope(Dispatchers.IO).launch {
            convertHistoryRepository.insertConvertHistory(convertHistoryData)
        }
    }
}
