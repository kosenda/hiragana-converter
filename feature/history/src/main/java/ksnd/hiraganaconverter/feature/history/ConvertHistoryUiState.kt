package ksnd.hiraganaconverter.feature.history

import androidx.compose.runtime.Immutable
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

@Immutable
data class ConvertHistoryUiState(
    val convertHistories: List<ConvertHistoryData> = emptyList(),
    val isShowDetailDialog: Boolean = false,
    val usedHistoryDataByDetail: ConvertHistoryData? = null,
)
