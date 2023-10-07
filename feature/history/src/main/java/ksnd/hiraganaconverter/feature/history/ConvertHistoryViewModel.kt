package ksnd.hiraganaconverter.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import javax.inject.Inject

@HiltViewModel
class ConvertHistoryViewModel @Inject constructor(
    private val convertHistoryRepository: ConvertHistoryRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConvertHistoryUiState())
    val uiState: StateFlow<ConvertHistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            convertHistoryRepository.observeAllConvertHistory().collect { convertHistories ->
                _uiState.update { it.copy(convertHistories = convertHistories.sortedByDescending { data -> data.id }) }
            }
        }
    }

    fun deleteAllConvertHistory() {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteAllConvertHistory()
        }
    }

    fun deleteConvertHistory(historyData: ConvertHistoryData) {
        CoroutineScope(ioDispatcher).launch {
            convertHistoryRepository.deleteConvertHistory(historyData.id)
        }
    }

    fun closeConvertHistoryDetailDialog() {
        _uiState.update {
            it.copy(
                isShowDetailDialog = false,
                usedHistoryDataByDetail = null,
            )
        }
    }

    fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData) {
        _uiState.update {
            it.copy(
                isShowDetailDialog = true,
                usedHistoryDataByDetail = historyData,
            )
        }
    }
}

