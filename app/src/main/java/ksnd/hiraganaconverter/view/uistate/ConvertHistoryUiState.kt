package ksnd.hiraganaconverter.view.uistate

import ksnd.hiraganaconverter.model.ConvertHistoryData

data class ConvertHistoryUiState(
    val convertHistories: List<ConvertHistoryData> = emptyList(),
    val isShowDetailDialog: Boolean = false,
    val usedHistoryDataByDetail: ConvertHistoryData? = null,
)
