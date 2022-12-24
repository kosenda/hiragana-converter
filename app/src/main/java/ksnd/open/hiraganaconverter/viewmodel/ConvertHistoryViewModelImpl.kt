package ksnd.open.hiraganaconverter.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.di.module.IODispatcher
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.open.hiraganaconverter.view.uistate.ConvertHistoryUiState
import javax.inject.Inject

@HiltViewModel
class ConvertHistoryViewModelImpl @Inject constructor(
    private val convertHistoryRepository: ConvertHistoryRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ConvertHistoryViewModel() {

    private val _uiState = MutableStateFlow(ConvertHistoryUiState())
    override val uiState: StateFlow<ConvertHistoryUiState> = _uiState.asStateFlow()

    override fun deleteAllConvertHistory() {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteAllConvertHistory()
        }
        _uiState.update { it.copy(convertHistories = emptyList()) }
    }

    override fun deleteConvertHistory(historyData: ConvertHistoryData) {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteConvertHistory(historyData.id)
        }
        _uiState.update {
            val newList = it.convertHistories.toMutableList()
            newList.removeIf { deleteTarget -> deleteTarget.id == historyData.id }
            it.copy(convertHistories = newList)
        }
    }

    override fun getAllConvertHistory() {
        CoroutineScope(ioDispatcher).launch {
            _uiState.update {
                it.copy(
                    convertHistories = convertHistoryRepository
                        .getAllConvertHistory()
                        .sortedByDescending { data -> data.id },
                )
            }
        }
    }

    override fun closeConvertHistoryDetailDialog() {
        _uiState.update {
            it.copy(
                isShowDetailDialog = false,
                usedHistoryDataByDetail = null,
            )
        }
    }

    override fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData) {
        _uiState.update {
            it.copy(
                isShowDetailDialog = true,
                usedHistoryDataByDetail = historyData,
            )
        }
    }
}
