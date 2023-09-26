package ksnd.hiraganaconverter.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import ksnd.hiraganaconverter.core.model.ConvertHistoryData
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.data.mock.MockConvertHistories
import ksnd.hiraganaconverter.view.uistate.ConvertHistoryUiState

class PreviewSettingViewModel : SettingsViewModel() {
    override val fontType = MutableStateFlow(FontType.YUSEI_MAGIC)
    override val theme = MutableStateFlow(Theme.AUTO)
    override val enableInAppUpdate: Flow<Boolean> = flowOf(true)
    override fun updateTheme(newTheme: Theme) {}
    override fun updateFontType(newFontType: FontType) {}
    override fun updateUseInAppUpdate(isEnabled: Boolean) {}
}

class PreviewConvertHistoryViewModel(isNoData: Boolean = false) : ConvertHistoryViewModel() {
    override val uiState: StateFlow<ConvertHistoryUiState> = MutableStateFlow(
        ConvertHistoryUiState(
            convertHistories =
            if (isNoData) {
                emptyList()
            } else {
                MockConvertHistories().data
            },
        ),
    ).asStateFlow()
    override fun deleteAllConvertHistory() {}
    override fun deleteConvertHistory(historyData: ConvertHistoryData) {}
    override fun closeConvertHistoryDetailDialog() {}
    override fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData) {}
}
