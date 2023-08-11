package ksnd.hiraganaconverter.model.repository

import ksnd.hiraganaconverter.model.ConvertHistoryData

interface ConvertHistoryRepository {
    fun insertConvertHistory(beforeText: String, afterText: String)
    fun getAllConvertHistory(): List<ConvertHistoryData>
    fun deleteAllConvertHistory()
    fun deleteConvertHistory(id: Long)
}
