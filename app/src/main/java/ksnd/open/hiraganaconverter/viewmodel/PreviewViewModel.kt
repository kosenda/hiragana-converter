package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import ksnd.open.hiraganaconverter.view.uistate.ConvertHistoryUiState
import ksnd.open.hiraganaconverter.view.uistate.ConvertUiState

class PreviewConvertViewModel : ConvertViewModel() {
    override val uiState: StateFlow<ConvertUiState> = MutableStateFlow(
        ConvertUiState(errorText = "失敗"),
    ).asStateFlow()
    override fun convert(context: Context) {}
    override fun updateInputText(inputText: String) {}
    override fun updateOutputText(outputText: String) {}
    override fun clearErrorText() {}
    override fun changeHiraKanaType(type: HiraKanaType) {}
}

class PreviewSettingViewModel : SettingsViewModel() {
    override val customFont: MutableState<String> = mutableStateOf(CustomFont.DEFAULT.name)
    override val themeNum: MutableState<Int> = mutableStateOf(ThemeNum.AUTO.num)
    override fun updateThemeNum(newThemeNum: Int) {}
    override fun updateCustomFont(newCustomFont: CustomFont) {}
    override fun isSelectedThemeNum(index: Int): Boolean = 0 == index
    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return CustomFont.DEFAULT == targetCustomFont
    }
}

class PreviewSelectLanguageViewModel : SelectLanguageViewModel() {
    override fun updateSelectLanguage(newLanguage: String) {}
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
