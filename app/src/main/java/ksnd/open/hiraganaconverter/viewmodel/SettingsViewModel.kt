package ksnd.open.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import ksnd.open.hiraganaconverter.view.CustomFont

abstract class SettingsViewModel : ViewModel() {
    abstract val customFont: String
    abstract val themeNum: Int
    abstract fun updateThemeNum(newThemeNum: Int)
    abstract fun updateCustomFont(newCustomFont: CustomFont)
    abstract fun getThemeNum()
    abstract fun getCustomFont()
    abstract fun isSelectedThemeNum(index: Int): Boolean
    abstract fun isSelectedFont(targetCustomFont: CustomFont): Boolean
}
