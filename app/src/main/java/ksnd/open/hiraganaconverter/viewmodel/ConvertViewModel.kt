package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.model.ResponseData
import retrofit2.Response

abstract class ConvertViewModel : ViewModel() {
    abstract val previousInputText: MutableState<String>
    abstract val inputText: MutableState<String>
    abstract val outputText: MutableState<String>
    abstract val errorText: MutableState<String>
    abstract val selectedTextType: MutableState<HiraKanaType>
    abstract val raw: MutableState<Response<ResponseData>?>
    abstract fun convert(context: Context, oldLastConvertTime: String, oldConvertCount: Int)
    abstract fun updateErrorText(context: Context)
}
