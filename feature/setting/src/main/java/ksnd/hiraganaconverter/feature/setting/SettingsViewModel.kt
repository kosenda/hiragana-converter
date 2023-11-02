package ksnd.hiraganaconverter.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val analytics: Analytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val uiState = combine(
        dataStoreRepository.theme(),
        dataStoreRepository.fontType(),
        dataStoreRepository.enableInAppUpdate(),
    ) { theme, fontType, enableInAppUpdate ->
        SettingsUiState(
            theme = theme,
            fontType = fontType,
            enableInAppUpdate = enableInAppUpdate,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingsUiState(),
    )

    fun updateTheme(newTheme: Theme) {
        if (uiState.value.theme != newTheme) analytics.logUpdateTheme(newTheme.name)
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateTheme(newTheme)
        }
    }

    fun updateFontType(newFontType: FontType) {
        if (uiState.value.fontType != newFontType) analytics.logUpdateFont(newFontType.name)
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateFontType(newFontType)
        }
    }

    fun updateEnableInAppUpdate(isEnabled: Boolean) {
        if (uiState.value.enableInAppUpdate != isEnabled) analytics.logSwitchEnableInAppUpdate(isEnabled)
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateUseInAppUpdate(isEnabled)
        }
    }
}
