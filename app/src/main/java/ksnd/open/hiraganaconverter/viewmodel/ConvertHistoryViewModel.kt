package ksnd.open.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.view.uistate.ConvertHistoryUiState

abstract class ConvertHistoryViewModel : ViewModel() {
    abstract val uiState: StateFlow<ConvertHistoryUiState>
    abstract fun deleteAllConvertHistory()
    abstract fun deleteConvertHistory(historyData: ConvertHistoryData)
    abstract fun getAllConvertHistory()
    abstract fun closeConvertHistoryDetailDialog()
    abstract fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData)
}
