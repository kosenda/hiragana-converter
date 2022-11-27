package ksnd.open.hiraganaconverter.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import ksnd.open.hiraganaconverter.model.ConvertHistoryData

abstract class ConvertHistoryViewModel : ViewModel() {
    abstract val convertHistories: MutableState<List<ConvertHistoryData>>
    abstract fun getAllConvertHistory()
    abstract fun deleteAllConvertHistory()
    abstract fun deleteConvertHistory(id: Long)
}
