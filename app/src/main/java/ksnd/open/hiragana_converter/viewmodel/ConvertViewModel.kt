package ksnd.open.hiragana_converter.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateFormat
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ksnd.open.hiragana_converter.BuildConfig
import ksnd.open.hiragana_converter.R
import ksnd.open.hiragana_converter.model.ResponseData
import ksnd.open.hiragana_converter.model.Type
import ksnd.open.hiragana_converter.model.repository.ConvertRepository
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    val previousInputText: MutableState<String> = mutableStateOf("")
    val inputText:         MutableState<String> = mutableStateOf("")
    val outputText:        MutableState<String> = mutableStateOf("")
    val errorText:         MutableState<String> = mutableStateOf("")
    val selectedTextType:  MutableState<Type> = mutableStateOf(Type.HIRAGANA)
    val raw:               MutableState<Response<ResponseData>?> =mutableStateOf(null)

    fun convert(context: Context) {
        // 入力値なしまたは前回入力値のままであるときは後続処理を行わない
        if(inputText.value == "" || previousInputText.value == inputText.value) {
            return
        }
        if(limitIsReached()) {
            errorText.value = context.getString(R.string.limit_local_count)
        } else {
            val appId = BuildConfig.apiKey
            CoroutineScope(Dispatchers.IO).launch {
                raw.value = convertRepository.requestConvert(
                    sentence = inputText.value,
                    type = selectedTextType.value.name.lowercase(Locale.ENGLISH),
                    appId = appId
                )
                outputText.value = raw.value?.body()?.converted ?: ""
                previousInputText.value = inputText.value
            }
        }
    }


    fun updateErrorText(context: Context) {
        Log.i("raw", raw.value?.raw().toString())
        when(raw.value?.code()) {
            null -> { errorText.value = "" }
            200 -> { errorText.value = "" }
            413 -> { errorText.value = context.getString(R.string.request_too_large) }
            400 -> {
                errorText.value = when(raw.value?.message()) {
                    "Rate limit exceeded" -> { context.getString(R.string.limit_exceeded) }
                    else -> { context.getString(R.string.conversion_failed) }
                }
            }
            else -> { errorText.value = context.getString(R.string.conversion_failed) }
        }
    }

    /**
     * 制限回数(１日あたり２００回)に達していることを確認する処理
     */
    private fun limitIsReached(): Boolean {
        val lastSearchTime = sharedPreferences.getString("last_search_time", "")
        val now = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val todayCount = if (lastSearchTime != now) { 1 } else {
            sharedPreferences.getInt("search_count", 0) + 1
        }
        sharedPreferences.edit().putInt("search_count", todayCount).apply()
        Log.i("search_count", todayCount.toString())
        sharedPreferences.edit().putString("last_search_time", now).apply()
        Log.i("last_search_time", now)
        return todayCount > 200
    }

}