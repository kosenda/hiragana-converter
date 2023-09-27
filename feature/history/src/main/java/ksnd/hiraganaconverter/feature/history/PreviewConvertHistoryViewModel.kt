package ksnd.hiraganaconverter.feature.history

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

class PreviewConvertHistoryViewModel(isNoData: Boolean = false) : ConvertHistoryViewModel() {
    override val uiState: StateFlow<ConvertHistoryUiState> = MutableStateFlow(
        ConvertHistoryUiState(
            convertHistories =
            if (isNoData) {
                emptyList()
            } else {
                MockConvertHistories().data
            },
        ),
    ).asStateFlow()
    override fun deleteAllConvertHistory() {}
    override fun deleteConvertHistory(historyData: ConvertHistoryData) {}
    override fun closeConvertHistoryDetailDialog() {}
    override fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData) {}
}
