package ksnd.hiraganaconverter.data.repository

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import ksnd.hiraganaconverter.data.database.ConvertHistoryDao
import ksnd.hiraganaconverter.data.database.RoomRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertHistoryRepositoryImplTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get: Rule
    val roomRule = RoomRule(context = ApplicationProvider.getApplicationContext())

    private val convertHistoryDao = spyk<ConvertHistoryDao>(roomRule.dao)

    private val convertHistoryRepositoryImpl = ConvertHistoryRepositoryImpl(convertHistoryDao = convertHistoryDao)

    @Test
    fun observeAllConvertHistory_initial_isEmpty() = runTest {
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().firstOrNull()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.observeAllConvertHistory() }
    }

    @Test
    fun insertConvertHistory_addHistoryOnce_size1() = runTest {
        convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT)
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().first().size).isEqualTo(1)
        verify(exactly = 1) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun insertConvertHistory_add3Histories_size3() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().first().size).isEqualTo(3)
        verify(exactly = 3) { convertHistoryDao.insertConvertHistory(any()) }
    }

    @Test
    fun deleteAllConvertHistory_exist3Histories_isEmpty() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        convertHistoryRepositoryImpl.deleteAllConvertHistory()
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().firstOrNull()).isEmpty()
        verify(exactly = 1) { convertHistoryDao.deleteAllConvertHistory() }
    }

    @Test
    fun convertHistoryRepository_deleteOnce_sizeMinus1() = runTest {
        repeat(3) { convertHistoryRepositoryImpl.insertConvertHistory(beforeText = BEFORE_TEXT, afterText = AFTER_TEXT) }
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().first().size).isEqualTo(3)
        val deleteHistoryId = convertHistoryRepositoryImpl.observeAllConvertHistory().first().first().id
        convertHistoryRepositoryImpl.deleteConvertHistory(id = deleteHistoryId)
        assertThat(convertHistoryRepositoryImpl.observeAllConvertHistory().first().size).isEqualTo(2)
    }

    companion object {
        private const val BEFORE_TEXT = "A"
        private const val AFTER_TEXT = "えー"
    }
}
