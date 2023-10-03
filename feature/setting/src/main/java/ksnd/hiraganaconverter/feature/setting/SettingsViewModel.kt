package ksnd.hiraganaconverter.feature.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val uiState = combine(
        dataStoreRepository.selectedTheme(),
        dataStoreRepository.selectedFontType(),
        dataStoreRepository.enableInAppUpdate(),
    ) { theme, fontType, enableInAppUpdate ->
        SettingsUiState(
            theme = theme,
            fontType = fontType,
            enableInAppUpdate = enableInAppUpdate,
        )
    }

    fun updateTheme(newTheme: Theme) {
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateTheme(newTheme)
        }
    }

    fun updateFontType(newFontType: FontType) {
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateFontType(newFontType)
        }
    }

    fun updateUseInAppUpdate(isEnabled: Boolean) {
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateUseInAppUpdate(isEnabled)
        }
    }
}
