package ksnd.open.hiraganaconverter.model.repository

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ksnd.open.hiraganaconverter.model.ConvertHistoryDao
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertHistoryRepositoryImplTest {
    private val convertHistoryRepositoryImpl = ConvertHistoryRepositoryImpl(
        convertHistoryDao = FakeConvertHistoryDao()
    )

    @Test
    fun convertHistoryRepository_initial_size0() {
        assertTrue(convertHistoryRepositoryImpl.getAllConvertHistory().isEmpty())
    }

    // ● insertConvertHistory & getAllConvertHistory--------------------------------------------- ●
    @Test
    fun convertHistoryRepository_addHistoryOnce_size1() {
        convertHistoryRepositoryImpl.insertConvertHistory(
            beforeText = "A",
            afterText = "B",
            time = "2022/12/20 22:10"
        )
        assertTrue(convertHistoryRepositoryImpl.getAllConvertHistory().size == 1)
    }

    @Test
    fun convertHistoryRepository_add3Histories_size3() {
        repeat(3) {
            convertHistoryRepositoryImpl.insertConvertHistory(
                beforeText = "A",
                afterText = "B",
                time = "2022/12/20 22:10"
            )
        }
        assertTrue(convertHistoryRepositoryImpl.getAllConvertHistory().size == 3)
    }

    // ● deleteAllConvertHistory ---------------------------------------------------------------- ●
    @Test
    fun convertHistoryRepository_deleteAll_size0() {
        repeat(3) {
            convertHistoryRepositoryImpl.insertConvertHistory(
                beforeText = "A",
                afterText = "B",
                time = "2022/12/20 22:10"
            )
        }
        convertHistoryRepositoryImpl.deleteAllConvertHistory()
        assertTrue(convertHistoryRepositoryImpl.getAllConvertHistory().isEmpty())
    }

    // ● deleteConvertHistory ------------------------------------------------------------------- ●
    @Test
    fun convertHistoryRepository_deleteOnce_sizeMinus1() {
        // 3回データを追加して一度
        repeat(3) {
            convertHistoryRepositoryImpl.insertConvertHistory(
                beforeText = "A",
                afterText = "B",
                time = "2022/12/20 22:10"
            )
        }
        val histories = convertHistoryRepositoryImpl.getAllConvertHistory()
        convertHistoryRepositoryImpl.deleteConvertHistory(id = histories[1].id)
        assertTrue(convertHistoryRepositoryImpl.getAllConvertHistory().size == 2)
    }
}

private class FakeConvertHistoryDao : ConvertHistoryDao {
    val dataSize: MutableState<Long> = mutableStateOf(0L)

    @SuppressLint("MutableCollectionMutableState")
    val convertHistoryDataList = mutableStateOf(mutableListOf<ConvertHistoryData>())
    override fun insertConvertHistory(convertHistoryData: ConvertHistoryData) {
        // IDを重複させない
        val changedIdConvertHistoryData = convertHistoryData.copy(id = dataSize.value + 1L)
        convertHistoryDataList.value.add(changedIdConvertHistoryData)
        dataSize.value++
    }

    override fun getAllConvertHistory(): List<ConvertHistoryData> {
        return convertHistoryDataList.value
    }

    override fun deleteAllConvertHistory() {
        convertHistoryDataList.value.clear()
    }

    override fun deleteConvertHistory(id: Long) {
        convertHistoryDataList.value.removeIf { it.id == id }
    }
}
