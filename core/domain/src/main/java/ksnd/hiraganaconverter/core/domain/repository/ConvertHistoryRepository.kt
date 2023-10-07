package ksnd.hiraganaconverter.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

interface ConvertHistoryRepository {
    fun insertConvertHistory(beforeText: String, afterText: String)
    fun observeAllConvertHistory(): Flow<List<ConvertHistoryData>>
    fun deleteAllConvertHistory()
    fun deleteConvertHistory(id: Long)
}
