package ksnd.hiraganaconverter.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme

class PreviewSettingViewModel : SettingsViewModel() {
    override val fontType = MutableStateFlow(FontType.YUSEI_MAGIC)
    override val theme = MutableStateFlow(Theme.AUTO)
    override val enableInAppUpdate: Flow<Boolean> = flowOf(true)
    override fun updateTheme(newTheme: Theme) {}
    override fun updateFontType(newFontType: FontType) {}
    override fun updateUseInAppUpdate(isEnabled: Boolean) {}
}
