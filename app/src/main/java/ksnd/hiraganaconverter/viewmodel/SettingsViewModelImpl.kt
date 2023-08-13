package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _theme = MutableStateFlow(Theme.AUTO)
    override val theme = _theme.asStateFlow()
    private val _fontType = MutableStateFlow(FontType.YUSEI_MAGIC)
    override val fontType = _fontType.asStateFlow()
    override val enableInAppUpdate = dataStoreRepository.enableInAppUpdate()

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
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateFontType(newFontType)
        }
    }

    override fun updateUseInAppUpdate(isEnabled: Boolean) {
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateUseInAppUpdate(isEnabled)
        }
    }

    private fun collectTheme() {
        viewModelScope.launch {
            dataStoreRepository.selectedTheme().collect { _theme.value = it }
        }
    }

    private fun collectFontType() {
        viewModelScope.launch {
            dataStoreRepository.selectedFontType().collect { _fontType.value = it }
        }
    }
}
