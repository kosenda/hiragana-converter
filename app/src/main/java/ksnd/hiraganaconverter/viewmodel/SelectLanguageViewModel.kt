package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel

abstract class SelectLanguageViewModel : ViewModel() {
    abstract fun updateSelectLanguage(newLanguage: String)
}
