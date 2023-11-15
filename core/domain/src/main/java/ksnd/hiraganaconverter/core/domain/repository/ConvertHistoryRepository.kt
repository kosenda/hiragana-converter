package ksnd.hiraganaconverter.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

interface ConvertHistoryRepository {
    suspend fun insertConvertHistory(beforeText: String, afterText: String)
    fun observeAllConvertHistory(): Flow<List<ConvertHistoryData>>
    suspend fun deleteAllConvertHistory()
    suspend fun deleteConvertHistory(id: Long)
}
