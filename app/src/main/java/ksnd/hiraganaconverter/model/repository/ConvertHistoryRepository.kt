package ksnd.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.model.ConvertHistoryData

interface ConvertHistoryRepository {
    fun insertConvertHistory(beforeText: String, afterText: String)
    fun getAllConvertHistory(): Flow<List<ConvertHistoryData>>
    fun deleteAllConvertHistory()
    fun deleteConvertHistory(id: Long)
}
