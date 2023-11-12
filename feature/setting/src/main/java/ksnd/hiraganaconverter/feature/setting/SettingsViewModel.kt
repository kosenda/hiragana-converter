package ksnd.hiraganaconverter.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsHelper
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val analytics: AnalyticsHelper,
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
        if (uiState.value.theme != newTheme) analytics.logEvent(Analytics.UpdateTheme(newTheme.name))
        viewModelScope.launch {
            dataStoreRepository.updateTheme(newTheme)
        }
    }

    fun updateFontType(newFontType: FontType) {
        if (uiState.value.fontType != newFontType) analytics.logEvent(Analytics.UpdateFont(newFontType.name))
        viewModelScope.launch {
            dataStoreRepository.updateFontType(newFontType)
        }
    }

    fun updateEnableInAppUpdate(isEnabled: Boolean) {
        if (uiState.value.enableInAppUpdate != isEnabled) analytics.logEvent(Analytics.SwitchEnableInAppUpdate(isEnabled))
        viewModelScope.launch {
            dataStoreRepository.updateUseInAppUpdate(isEnabled)
        }
    }
}
