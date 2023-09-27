package ksnd.hiraganaconverter.feature.history

import ksnd.hiraganaconverter.core.model.ConvertHistoryData

data class ConvertHistoryUiState(
    val convertHistories: List<ConvertHistoryData> = emptyList(),
    val isShowDetailDialog: Boolean = false,
    val usedHistoryDataByDetail: ConvertHistoryData? = null,
)
