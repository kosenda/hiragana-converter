package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.di.module.IODispatcher
import ksnd.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : SettingsViewModel() {
    override val theme = MutableStateFlow(Theme.AUTO)
    override val fontType = MutableStateFlow(FontType.YUSEI_MAGIC)

    init {
        collectTheme()
        collectFontType()
    }

    override fun updateTheme(newTheme: Theme) {
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateTheme(newTheme)
        }
    }

    override fun updateFontType(newFontType: FontType) {
        fontType.value = newFontType
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateFontType(newFontType)
        }
    }

    private fun collectTheme() {
        viewModelScope.launch {
            dataStoreRepository.selectedTheme().collect { theme.value = it }
        }
    }

    private fun collectFontType() {
        viewModelScope.launch {
            dataStoreRepository.selectedTheme().collect {
                fontType.value = FontType.values().firstOrNull { fontType.value.fontName == it.fontName } ?: FontType.YUSEI_MAGIC
            }
        }
    }
}
