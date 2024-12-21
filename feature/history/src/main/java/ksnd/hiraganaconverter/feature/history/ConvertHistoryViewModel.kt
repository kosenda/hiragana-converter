package ksnd.hiraganaconverter.feature.history

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import javax.inject.Inject

@HiltViewModel
class ConvertHistoryViewModel @Inject constructor(
    private val convertHistoryRepository: ConvertHistoryRepository,
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
        viewModelScope.launch {
            convertHistoryRepository.deleteAllConvertHistory()
        }
    }

    fun deleteConvertHistory(historyData: ConvertHistoryData) {
        viewModelScope.launch {
            convertHistoryRepository.deleteConvertHistory(historyData.id)
        }
    }
}

@Immutable
data class ConvertHistoryUiState(
    val convertHistories: List<ConvertHistoryData> = emptyList(),
)
