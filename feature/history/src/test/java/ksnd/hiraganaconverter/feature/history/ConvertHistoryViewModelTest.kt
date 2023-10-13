package ksnd.hiraganaconverter.feature.history

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertHistoryViewModelTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertHistoryRepository = mockk<ConvertHistoryRepository>(relaxed = true)
    private val viewModel = ConvertHistoryViewModel(
        convertHistoryRepository = convertHistoryRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun deleteAllConvertHistory_once_isCalledDeleteAllConvertHistory() = runTest {
        viewModel.deleteAllConvertHistory()
        coVerify(exactly = 1) { convertHistoryRepository.deleteAllConvertHistory() }
    }

    @Test
    fun deleteConvertHistory_once_isCalledDeleteConvertHistory() = runTest {
        val testDate = MockConvertHistories().data.first()
        viewModel.deleteConvertHistory(historyData = testDate)
        coVerify(exactly = 1) { convertHistoryRepository.deleteConvertHistory(testDate.id) }
    }

    @Test
    fun uiState_initial_notShowDetailDialogAndNotUsedHistoryData() {
        assertThat(viewModel.uiState.value.isShowDetailDialog).isFalse()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNull()
    }

    @Test
    fun showConvertHistoryDetailDialog_convertedHistory_showDialogAndExistUsedData() = runTest {
        viewModel.showConvertHistoryDetailDialog(historyData = MockConvertHistories().data.first())
        assertThat(viewModel.uiState.value.isShowDetailDialog).isTrue()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNotNull()
    }

    @Test
    fun closeConvertHistoryDetailDialog_once_hideDialogAndNotExistUsedData() = runTest {
        viewModel.showConvertHistoryDetailDialog(historyData = MockConvertHistories().data.first())
        viewModel.closeConvertHistoryDetailDialog()
        assertThat(viewModel.uiState.value.isShowDetailDialog).isFalse()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNull()
    }
}
