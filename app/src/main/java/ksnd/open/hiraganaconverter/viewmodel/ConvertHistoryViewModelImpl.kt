package ksnd.open.hiraganaconverter.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import javax.inject.Inject

@HiltViewModel
class ConvertHistoryViewModelImpl @Inject constructor(
    private val convertHistoryRepository: ConvertHistoryRepository
) : ConvertHistoryViewModel() {

    init {
        getAllConvertHistory()
    }

    override val convertHistories: MutableState<List<ConvertHistoryData>> =
        mutableStateOf(emptyList())

    override fun deleteAllConvertHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            convertHistoryRepository.deleteAllConvertHistory()
        }
    }

    override fun deleteConvertHistory(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            convertHistoryRepository.deleteConvertHistory(id)
            val newList = convertHistories.value.toMutableList()
            newList.removeIf { it.id == id }
            convertHistories.value = newList
        }
    }

    private fun getAllConvertHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            convertHistories.value = convertHistoryRepository
                .getAllConvertHistory()
                .sortedByDescending { it.id }
        }
    }
}
