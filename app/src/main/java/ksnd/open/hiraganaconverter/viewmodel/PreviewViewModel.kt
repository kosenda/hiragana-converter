package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.view.ConvertUiState
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum

class PreviewConvertViewModel : ConvertViewModel() {
    override val uiState: StateFlow<ConvertUiState> = MutableStateFlow(
        ConvertUiState(errorText = "失敗")
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
    override fun getThemeNum() {}
    override fun getCustomFont() {}
    override fun isSelectedThemeNum(index: Int): Boolean = 0 == index
    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return CustomFont.DEFAULT == targetCustomFont
    }
}

class PreviewSelectLanguageViewModel : SelectLanguageViewModel() {
    override fun updateSelectLanguage(newLanguage: String) {}
}

class PreviewConvertHistoryViewModel(isNoData: Boolean = false) : ConvertHistoryViewModel() {
    override val convertHistories: MutableState<List<ConvertHistoryData>> = mutableStateOf(
        if (isNoData) {
            emptyList()
        } else {
            listOf(
                ConvertHistoryData(
                    id = 0,
                    time = "2022/11/26 21:34",
                    before = "漢字漢字漢字ひらがなひらがなひらがなカタカナカタカナカタカナEnglishEnglishEnglish",
                    after = "ここはつかわれない"
                ),
                ConvertHistoryData(
                    id = 1,
                    time = "2022/11/27 11:42",
                    before = "漢字漢字漢字",
                    after = "ここはつかわれない"
                )
            )
        }
    )
    override fun getAllConvertHistory() {}
    override fun deleteAllConvertHistory() {}
    override fun deleteConvertHistory(id: Long) {}
}
