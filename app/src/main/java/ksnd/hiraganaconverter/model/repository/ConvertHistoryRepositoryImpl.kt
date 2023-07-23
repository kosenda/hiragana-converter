package ksnd.hiraganaconverter.model.repository

import ksnd.hiraganaconverter.model.ConvertHistoryDao
import ksnd.hiraganaconverter.model.ConvertHistoryData
import javax.inject.Inject

class ConvertHistoryRepositoryImpl @Inject constructor(
    private val convertHistoryDao: ConvertHistoryDao,
) : ConvertHistoryRepository {
    override fun insertConvertHistory(beforeText: String, afterText: String, time: String) {
        convertHistoryDao.insertConvertHistory(
            convertHistoryData = ConvertHistoryData(
                before = beforeText,
                after = afterText,
                time = time,
            ),
        )
    }

    override fun getAllConvertHistory(): List<ConvertHistoryData> =
        convertHistoryDao.getAllConvertHistory()

    override fun deleteAllConvertHistory() =
        convertHistoryDao.deleteAllConvertHistory()

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDao.deleteConvertHistory(id)
    }
}
