package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.di.module.IODispatcher
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.view.uistate.ConvertHistoryUiState
import javax.inject.Inject

@HiltViewModel
class ConvertHistoryViewModelImpl @Inject constructor(
    private val convertHistoryRepository: ConvertHistoryRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ConvertHistoryViewModel() {

    private val _uiState = MutableStateFlow(ConvertHistoryUiState())
    override val uiState: StateFlow<ConvertHistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            convertHistoryRepository.getAllConvertHistory().collect { convertHistories ->
                _uiState.update { it.copy(convertHistories = convertHistories.sortedByDescending { data -> data.id }) }
            }
        }
    }

    override fun deleteAllConvertHistory() {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteAllConvertHistory()
        }
    }

    override fun deleteConvertHistory(historyData: ConvertHistoryData) {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteConvertHistory(historyData.id)
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
