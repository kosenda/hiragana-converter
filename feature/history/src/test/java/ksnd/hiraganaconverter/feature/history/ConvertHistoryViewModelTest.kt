package ksnd.hiraganaconverter.feature.history

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.model.mock.MockConvertHistoryData
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertHistoryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertHistoryRepository = mockk<ConvertHistoryRepository>(relaxed = true)
    private val viewModel = ConvertHistoryViewModel(
        convertHistoryRepository = convertHistoryRepository,
    )

    @Test
    fun deleteAllConvertHistory_isCalledDeleteAllConvertHistory() = runTest {
        viewModel.deleteAllConvertHistory()

        coVerify(exactly = 1) { convertHistoryRepository.deleteAllConvertHistory() }
    }

    @Test
    fun deleteConvertHistory_isCalledDeleteConvertHistory() = runTest {
        viewModel.deleteConvertHistory(historyData = HISTORY_DATA)

        coVerify(exactly = 1) { convertHistoryRepository.deleteConvertHistory(HISTORY_DATA.id) }
    }

    private companion object {
        private val HISTORY_DATA = MockConvertHistoryData().data.first()
    }
}
