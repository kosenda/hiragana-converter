package ksnd.hiraganaconverter.core.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ksnd.hiraganaconverter.core.data.database.ConvertHistoryDao
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.TimeFormat
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConvertHistoryRepositoryImpl @Inject constructor(
    private val convertHistoryDao: ConvertHistoryDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ConvertHistoryRepository {
    override suspend fun insertConvertHistory(beforeText: String, afterText: String) = withContext(ioDispatcher) {
        convertHistoryDao.insertConvertHistory(
            convertHistoryData = ConvertHistoryData(
                before = beforeText,
                after = afterText,
                time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TimeFormat.YEAR_MONTH_DATE_HOUR_MINUTE.format)),
            ),
        )
    }

    override fun observeAllConvertHistory(): Flow<List<ConvertHistoryData>> = convertHistoryDao.observeAllConvertHistory()

    override suspend fun deleteAllConvertHistory() = withContext(ioDispatcher) {
        convertHistoryDao.deleteAllConvertHistory()
    }

    override suspend fun deleteConvertHistory(id: Long) = withContext(ioDispatcher) {
        convertHistoryDao.deleteConvertHistory(id)
    }
}
