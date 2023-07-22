package ksnd.hiraganaconverter.model.repository

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.truth.Truth.assertThat
import io.mockk.spyk
import io.mockk.verify
import ksnd.hiraganaconverter.model.ConvertHistoryDao
import ksnd.hiraganaconverter.model.ConvertHistoryData
import org.junit.Test

class ConvertHistoryRepositoryImplTest {

    private var convertHistoryDao = spyk<FakeConvertHistoryDao>()
    private val convertHistoryRepositoryImpl = ConvertHistoryRepositoryImpl(convertHistoryDao = convertHistoryDao)

    @Test
    fun getAllConvertHistory_initial_isEmpty() {
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.getAllConvertHistory() }
    }

    @Test
    fun insertConvertHistory_addHistoryOnce_size1() {
        convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT, time = TEST_DATE)
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().size).isEqualTo(1)
        verify(exactly = 1) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun insertConvertHistory_add3Histories_size3() {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT, time = TEST_DATE) }
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().size).isEqualTo(3)
        verify(exactly = 3) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun deleteAllConvertHistory_exist3Histories_isEmpty() {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT, time = TEST_DATE) }
        convertHistoryRepositoryImpl.deleteAllConvertHistory()
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.deleteAllConvertHistory() }
    }

    @Test
    fun convertHistoryRepository_deleteOnce_sizeMinus1() {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT, time = TEST_DATE) }
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().size).isEqualTo(3)
        val deleteHistoryId = convertHistoryRepositoryImpl.getAllConvertHistory().first().id
        convertHistoryRepositoryImpl.deleteConvertHistory(id = deleteHistoryId)
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().size).isEqualTo(2)
    }

    companion object {
        private const val BEFORE_TEXT = "A"
        private const val AFTER_TEXT = "えー"
        private const val TEST_DATE = "2022/12/20 22:10"
    }
}

@SuppressLint("MutableCollectionMutableState")
private class FakeConvertHistoryDao : ConvertHistoryDao {
    private var historyCount by mutableStateOf(0L)
    private var convertHistoryDataList by mutableStateOf(mutableListOf<ConvertHistoryData>())

    override fun insertConvertHistory(convertHistoryData: ConvertHistoryData) {
        // Do not duplicate id
        historyCount++
        val changedIdConvertHistoryData = convertHistoryData.copy(id = historyCount)
        convertHistoryDataList.add(changedIdConvertHistoryData)
    }

    override fun getAllConvertHistory(): List<ConvertHistoryData> {
        return convertHistoryDataList
    }

    override fun deleteAllConvertHistory() {
        convertHistoryDataList.clear()
    }

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDataList.removeIf { it.id == id }
    }
}
