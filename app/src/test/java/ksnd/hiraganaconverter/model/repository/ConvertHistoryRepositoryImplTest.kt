package ksnd.hiraganaconverter.model.repository

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.truth.Truth.assertThat
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.model.ConvertHistoryDao
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertHistoryRepositoryImplTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var convertHistoryDao = spyk<FakeConvertHistoryDao>()
    private val convertHistoryRepositoryImpl = ConvertHistoryRepositoryImpl(convertHistoryDao = convertHistoryDao)

    @Test
    fun getAllConvertHistory_initial_isEmpty() = runTest {
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().firstOrNull()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.getAllConvertHistory() }
    }

    @Test
    fun insertConvertHistory_addHistoryOnce_size1() = runTest {
        convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT)
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().first().size).isEqualTo(1)
        verify(exactly = 1) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun insertConvertHistory_add3Histories_size3() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().first().size).isEqualTo(3)
        verify(exactly = 3) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun deleteAllConvertHistory_exist3Histories_isEmpty() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        convertHistoryRepositoryImpl.deleteAllConvertHistory()
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().firstOrNull()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.deleteAllConvertHistory() }
    }

    @Test
    fun convertHistoryRepository_deleteOnce_sizeMinus1() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().first().size).isEqualTo(3)
        val deleteHistoryId = convertHistoryRepositoryImpl.getAllConvertHistory().first().first().id
        convertHistoryRepositoryImpl.deleteConvertHistory(id = deleteHistoryId)
        assertThat(convertHistoryRepositoryImpl.getAllConvertHistory().first().size).isEqualTo(2)
    }

    companion object {
        private const val BEFORE_TEXT = "A"
        private const val AFTER_TEXT = "えー"
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

    override fun getAllConvertHistory(): Flow<List<ConvertHistoryData>> {
        return flowOf(convertHistoryDataList)
    }

    override fun deleteAllConvertHistory() {
        convertHistoryDataList.clear()
    }

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDataList.removeIf { it.id == id }
    }
}
