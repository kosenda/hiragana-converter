package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepository
import org.junit.Rule
import org.junit.Test

class ConvertHistoryViewModelImplTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertHistoryRepository = mockk<ConvertHistoryRepository>(relaxUnitFun = true)
    private val viewModel = ConvertHistoryViewModelImpl(
        convertHistoryRepository = convertHistoryRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun getAllConvertHistory_initial_isEmpty() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns emptyList()
        viewModel.getAllConvertHistory()
        assertThat(viewModel.uiState.value.convertHistories).isEmpty()
        coVerify(exactly = 1) { convertHistoryRepository.getAllConvertHistory() }
    }

    @Test
    fun getAllConvertHistory_existHistories_isEmpty() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns convertHistories
        viewModel.getAllConvertHistory()
        assertThat(viewModel.uiState.value.convertHistories).isNotEmpty()
        coVerify(exactly = 1) { convertHistoryRepository.getAllConvertHistory() }
    }

    @Test
    fun deleteConvertHistory_deleteOnce_decrease1() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns convertHistories
        viewModel.getAllConvertHistory()
        val initialHistoriesSize = convertHistories.size
        viewModel.deleteConvertHistory(viewModel.uiState.value.convertHistories.first())
        assertThat(viewModel.uiState.value.convertHistories.size).isEqualTo(initialHistoriesSize - 1)
        coVerify(exactly = 1) { convertHistoryRepository.deleteConvertHistory(any()) }
    }

    @Test
    fun deleteAllConvertHistory_existData_isEmpty() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns convertHistories
        viewModel.getAllConvertHistory()
        assertThat(viewModel.uiState.value.convertHistories).isNotEmpty()
        viewModel.deleteAllConvertHistory()
        assertThat(viewModel.uiState.value.convertHistories).isEmpty()
        coVerify(exactly = 1) { convertHistoryRepository.deleteAllConvertHistory() }
    }

    @Test
    fun uiState_initial_notShowDetailDialogAndNotUsedHistoryData() {
        assertThat(viewModel.uiState.value.isShowDetailDialog).isFalse()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNull()
    }

    @Test
    fun showConvertHistoryDetailDialog_convertedHistory_showDialogAndExistUsedData() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns convertHistories
        viewModel.getAllConvertHistory()
        viewModel.showConvertHistoryDetailDialog(historyData = viewModel.uiState.value.convertHistories.first())
        assertThat(viewModel.uiState.value.isShowDetailDialog).isTrue()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNotNull()
    }

    @Test
    fun closeConvertHistoryDetailDialog_once_hideDialogAndNotExistUsedData() = runTest {
        coEvery { convertHistoryRepository.getAllConvertHistory() } returns convertHistories
        viewModel.getAllConvertHistory()
        viewModel.showConvertHistoryDetailDialog(historyData = viewModel.uiState.value.convertHistories.first())
        viewModel.closeConvertHistoryDetailDialog()
        assertThat(viewModel.uiState.value.isShowDetailDialog).isFalse()
        assertThat(viewModel.uiState.value.usedHistoryDataByDetail).isNull()
    }

    companion object {
        private val convertHistories = listOf(
            ConvertHistoryData(id = 0, time = "2022/12/10 10:49", "日本語", "にほんご"),
            ConvertHistoryData(id = 1, time = "2022/12/10 10:50", "英語", "エイゴ"),
        )
    }
}
