package ksnd.open.hiraganaconverter.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConvertHistoryViewModelImplTest {

    private val testDispatcher = StandardTestDispatcher()

    // 既存のデータ（変換履歴）あり（２件）
    private val existInitDataViewModel = ConvertHistoryViewModelImpl(
        convertHistoryRepository = FakeConvertHistoryRepository(exitsInitData = true),
        ioDispatcher = testDispatcher
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ■ データ（変換履歴）に関するテスト ------------------------------------------------------ START

    @Test
    fun convertHistoryViewModel_Initialization_NotExistInitData() = runTest {
        // 既存のデータ（変換履歴）なし
        val notExistInitDataViewModel = ConvertHistoryViewModelImpl(
            convertHistoryRepository = FakeConvertHistoryRepository(exitsInitData = false),
            ioDispatcher = testDispatcher
        )
        // 初期化時にデータ（変換履歴）が保存されていなかった場合、UiStateにデータが設定されていないことを確認
        assertTrue(notExistInitDataViewModel.uiState.value.convertHistories.isEmpty())
        notExistInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        assertTrue(notExistInitDataViewModel.uiState.value.convertHistories.isEmpty())
    }

    @Test
    fun convertHistoryViewModel_Initialization_ExistInitData() = runTest {
        assertTrue(existInitDataViewModel.uiState.value.convertHistories.isEmpty())
        // 初期化時にデータ（変換履歴）が保存されていた場合、UiStateにデータが設定されていることを確認
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        assertTrue(existInitDataViewModel.uiState.value.convertHistories.isNotEmpty())
    }

    @Test
    fun convertHistoryViewModel_deleteHistoryData_countDown() = runTest {
        // 既存の２件データの内１件を消したときにuiStateのデータのサイズが１つ減っていることを確認
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        val initializedSize = existInitDataViewModel.uiState.value.convertHistories.size
        val afterAddDataSize = initializedSize - 1
        existInitDataViewModel.deleteConvertHistory(
            existInitDataViewModel.uiState.value.convertHistories.first()
        )
        advanceUntilIdle()
        assertTrue(existInitDataViewModel.uiState.value.convertHistories.size == afterAddDataSize)
    }

    @Test
    fun convertHistoryViewModel_deleteAllHistoryData_countZero() = runTest {
        // 既存データ（２件）を全て消したときにuiStateのデータのサイズがゼロになっていることを確認
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        existInitDataViewModel.deleteAllConvertHistory()
        advanceUntilIdle()
        assertTrue(existInitDataViewModel.uiState.value.convertHistories.isEmpty())
    }

    // ■ データ（変換履歴）に関するテスト ------------------------------------------------------ End

    // ■ 変換履歴の詳細ダイアログに関するテスト ------------------------------------------------- Start

    @Test
    fun convertHistoryViewModel_Initialization_notShowDetailDialogAndNotUsedData() = runTest {
        // 初期化
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        // 初期化時はダイアログが非表示かつダイアログで使われるデータがuiStateにないことを確認
        assertFalse(existInitDataViewModel.uiState.value.isShowDetailDialog)
        assertTrue(existInitDataViewModel.uiState.value.usedHistoryDataByDetail == null)
    }

    @Test
    fun convertHistoryViewModel_showDetailDialog_usedData() = runTest {
        // 初期化
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        // ダイアログが表示されたときにダイアログで使われるデータがuiStateにあることを確認
        existInitDataViewModel.showConvertHistoryDetailDialog(
            historyData = existInitDataViewModel.uiState.value.convertHistories.first()
        )
        assertTrue(existInitDataViewModel.uiState.value.isShowDetailDialog)
        assertFalse(existInitDataViewModel.uiState.value.usedHistoryDataByDetail == null)
    }

    @Test
    fun convertHistoryViewModel_closeDetailDialog_notUsedData() = runTest {
        // 初期化
        existInitDataViewModel.getAllConvertHistory()
        advanceUntilIdle()
        // ダイアログが非表示したときにダイアログで使われるデータがuiStateにないことを確認
        existInitDataViewModel.showConvertHistoryDetailDialog(
            historyData = existInitDataViewModel.uiState.value.convertHistories.first()
        )
        existInitDataViewModel.closeConvertHistoryDetailDialog()
        assertFalse(existInitDataViewModel.uiState.value.isShowDetailDialog)
        assertTrue(existInitDataViewModel.uiState.value.usedHistoryDataByDetail == null)
    }

    // ■ 変換履歴の詳細ダイアログに関するテスト ------------------------------------------------- End
}

private class FakeConvertHistoryRepository(exitsInitData: Boolean) : ConvertHistoryRepository {
    private var testData: MutableList<ConvertHistoryData> = mutableListOf()

    init {
        if (exitsInitData) {
            testData.add(ConvertHistoryData(id = 0, time = "2022/12/10 10:49", "亜", "あ"))
            testData.add(ConvertHistoryData(id = 1, time = "2022/12/10 10:50", "位", "イ"))
        }
    }

    override fun insertConvertHistory(beforeText: String, afterText: String, time: String) {
        testData.add(
            ConvertHistoryData(
                id = testData.size.toLong(),
                before = beforeText,
                after = afterText,
                time = time
            )
        )
    }

    override fun getAllConvertHistory(): List<ConvertHistoryData> {
        return testData
    }

    override fun deleteAllConvertHistory() {
        testData = mutableListOf()
    }

    override fun deleteConvertHistory(id: Long) {
        testData.removeIf { deleteTarget -> deleteTarget.id == id }
    }
}
