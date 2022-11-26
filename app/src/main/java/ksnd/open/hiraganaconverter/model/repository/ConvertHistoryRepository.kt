package ksnd.open.hiraganaconverter.model.repository

import ksnd.open.hiraganaconverter.model.ConvertHistoryData

interface ConvertHistoryRepository {
    fun insertConvertHistory(convertHistoryData: ConvertHistoryData)
    fun getAllConvertHistory(): List<ConvertHistoryData>
    fun deleteAllConvertHistory()
}
