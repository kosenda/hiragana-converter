package ksnd.hiraganaconverter.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import ksnd.hiraganaconverter.mock.data.MockConvertHistories
import ksnd.hiraganaconverter.model.ConvertErrorType
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme
import ksnd.hiraganaconverter.view.uistate.ConvertHistoryUiState
import ksnd.hiraganaconverter.view.uistate.ConvertUiState

class PreviewConvertViewModel : ConvertViewModel() {
    override val uiState: StateFlow<ConvertUiState> = MutableStateFlow(
        ConvertUiState(convertErrorType = ConvertErrorType.CONVERSION_FAILED),
    ).asStateFlow()
    override fun convert() {}
    override fun updateInputText(inputText: String) {}
    override fun updateOutputText(outputText: String) {}
    override fun clearConvertErrorType() {}
    override fun clearAllText() {}

    override fun changeHiraKanaType(type: HiraKanaType) {}
}

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
