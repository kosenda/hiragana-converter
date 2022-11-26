package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ksnd.open.hiraganaconverter.model.ConvertHistoryData
import ksnd.open.hiraganaconverter.model.HiraKanaType
import ksnd.open.hiraganaconverter.model.ResponseData
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import retrofit2.Response

class PreviewConvertViewModel : ConvertViewModel() {
    override val previousInputText: MutableState<String> = mutableStateOf("")
    override val inputText: MutableState<String> = mutableStateOf("入力値")
    override val outputText: MutableState<String> = mutableStateOf("にゅうりょくち")
    override val errorText: MutableState<String> = mutableStateOf("エラー表示はこんなかんじ")
    override val selectedTextType: MutableState<HiraKanaType> =
        mutableStateOf(HiraKanaType.HIRAGANA)
    override val raw: MutableState<Response<ResponseData>?> = mutableStateOf(null)
    override fun convert(context: Context) {}
    override fun updateErrorText(context: Context) {}
    override fun insertConvertHistory(beforeText: String, afterText: String, context: Context) {}
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
    private val fakeData = ConvertHistoryData(
        id = 0,
        time = "2022/11/26 21:34",
        before = "漢字漢字漢字ひらがなひらがなひらがなカタカナカタカナカタカナEnglishEnglishEnglish",
        after = "ここはつかわれない"
    )
    override val convertHistories: MutableState<List<ConvertHistoryData>> = mutableStateOf(
        if (isNoData) {
            emptyList()
        } else {
            listOf(
                fakeData,
                fakeData,
                fakeData
            )
        }
    )

    override fun getAllConvertHistory() {}
    override fun deleteAllConvertHistory() {}
}
