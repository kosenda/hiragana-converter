package ksnd.hiraganaconverter.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ksnd.hiraganaconverter.model.ConvertErrorType
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.view.CustomFont
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
    override val customFont: MutableState<String> = mutableStateOf(CustomFont.DEFAULT.name)
    override val themeNum: MutableState<Int> = mutableIntStateOf(Theme.AUTO.num)
    override fun updateThemeNum(newThemeNum: Int) {}
    override fun updateCustomFont(newCustomFont: CustomFont) {}
    override fun isSelectedThemeNum(index: Int): Boolean = 0 == index
    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return CustomFont.DEFAULT == targetCustomFont
    }
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
