package ksnd.hiraganaconverter.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import ksnd.hiraganaconverter.view.FontType

abstract class SettingsViewModel : ViewModel() {
    abstract val fontType: MutableState<FontType>
    abstract val theme: MutableState<Int>
    abstract fun updateTheme(newTheme: Int)
    abstract fun updateFontType(newFontType: FontType)
    abstract fun isSelectedTheme(index: Int): Boolean
    abstract fun isSelectedFontType(targetFontType: FontType): Boolean
}
