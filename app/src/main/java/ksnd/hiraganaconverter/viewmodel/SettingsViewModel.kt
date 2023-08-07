package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme

abstract class SettingsViewModel : ViewModel() {
    abstract val fontType: StateFlow<FontType>
    abstract val theme: StateFlow<Theme>
    abstract fun updateTheme(newTheme: Theme)
    abstract fun updateFontType(newFontType: FontType)
}
