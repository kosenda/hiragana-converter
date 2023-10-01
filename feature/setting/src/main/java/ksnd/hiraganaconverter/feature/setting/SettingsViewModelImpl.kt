package ksnd.hiraganaconverter.feature.setting

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
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
