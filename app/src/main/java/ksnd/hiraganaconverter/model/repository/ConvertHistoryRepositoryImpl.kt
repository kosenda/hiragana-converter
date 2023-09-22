package ksnd.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.model.ConvertHistoryDao
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.TimeFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConvertHistoryRepositoryImpl @Inject constructor(
    private val convertHistoryDao: ConvertHistoryDao,
) : ConvertHistoryRepository {
    override fun insertConvertHistory(beforeText: String, afterText: String) {
        convertHistoryDao.insertConvertHistory(
            convertHistoryData = ConvertHistoryData(
                before = beforeText,
                after = afterText,
                time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TimeFormat.YEAR_MONTH_DATE_HOUR_MINUTE.format)),
            ),
        )
    }

    override fun getAllConvertHistory(): Flow<List<ConvertHistoryData>> =
        convertHistoryDao.getAllConvertHistory()

    override fun deleteAllConvertHistory() =
        convertHistoryDao.deleteAllConvertHistory()

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDao.deleteConvertHistory(id)
    }
}
