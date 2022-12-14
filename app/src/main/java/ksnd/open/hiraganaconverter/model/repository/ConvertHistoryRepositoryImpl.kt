package ksnd.open.hiraganaconverter.model.repository

import ksnd.open.hiraganaconverter.model.ConvertHistoryDao
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import javax.inject.Inject

class ConvertHistoryRepositoryImpl @Inject constructor(
    private val convertHistoryDao: ConvertHistoryDao,
) : ConvertHistoryRepository {
    // ■ CREATE
    override fun insertConvertHistory(beforeText: String, afterText: String, time: String) {
        convertHistoryDao.insertConvertHistory(
            convertHistoryData = ConvertHistoryData(
                before = beforeText,
                after = afterText,
                time = time,
            ),
        )
    }

    // ■ READ
    override fun getAllConvertHistory(): List<ConvertHistoryData> =
        convertHistoryDao.getAllConvertHistory()

    // ■ DELETE
    override fun deleteAllConvertHistory() =
        convertHistoryDao.deleteAllConvertHistory()

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDao.deleteConvertHistory(id)
    }
}
