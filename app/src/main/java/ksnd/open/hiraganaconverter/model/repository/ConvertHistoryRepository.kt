package ksnd.open.hiraganaconverter.model.repository

import ksnd.open.hiraganaconverter.model.ConvertHistoryData

interface ConvertHistoryRepository {
    fun insertConvertHistory(beforeText: String, afterText: String, time: String)
    fun getAllConvertHistory(): List<ConvertHistoryData>
    fun deleteAllConvertHistory()
    fun deleteConvertHistory(id: Long)
}
