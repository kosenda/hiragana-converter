package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme

abstract class SettingsViewModel : ViewModel() {
    abstract val fontType: MutableStateFlow<FontType>
    abstract val theme: MutableStateFlow<Theme>
    abstract fun updateTheme(newTheme: Theme)
    abstract fun updateFontType(newFontType: FontType)
}
