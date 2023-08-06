package ksnd.hiraganaconverter.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    override fun convert(timeZone: String) {}
    override fun updateInputText(inputText: String) {}
    override fun updateOutputText(outputText: String) {}
    override fun clearConvertErrorType() {}
    override fun clearAllText() {}

    override fun changeHiraKanaType(type: HiraKanaType) {}
}

class PreviewSettingViewModel : SettingsViewModel() {
    override val fontType = MutableStateFlow(FontType.YUSEI_MAGIC)
    override val theme = MutableStateFlow(Theme.AUTO)
    override fun updateTheme(newTheme: Theme) {}
    override fun updateFontType(newFontType: FontType) {}
}

class PreviewConvertHistoryViewModel(isNoData: Boolean = false) : ConvertHistoryViewModel() {
    override val uiState: StateFlow<ConvertHistoryUiState> = MutableStateFlow(
        ConvertHistoryUiState(
            convertHistories =
            if (isNoData) {
                emptyList()
            } else {
                listOf(
                    ConvertHistoryData(
                        id = 0,
                        time = "2022/11/26 21:34",
                        before = "漢字漢字漢字ひらがなひらがなひらがなカタカナカタカナカタカナEnglishEnglishEnglish",
                        after = "ここはつかわれない",
                    ),
                    ConvertHistoryData(
                        id = 1,
                        time = "2022/11/27 11:42",
                        before = "漢字漢字漢字",
                        after = "ここはつかわれない",
                    ),
                )
            },
        ),
    ).asStateFlow()
    override fun deleteAllConvertHistory() {}
    override fun deleteConvertHistory(historyData: ConvertHistoryData) {}
    override fun getAllConvertHistory() {}
    override fun closeConvertHistoryDetailDialog() {}
    override fun showConvertHistoryDetailDialog(historyData: ConvertHistoryData) {}
}
