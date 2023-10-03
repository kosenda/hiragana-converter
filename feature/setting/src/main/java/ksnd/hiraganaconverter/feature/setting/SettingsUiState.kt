package ksnd.hiraganaconverter.feature.setting

import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme

data class SettingsUiState(
    val theme: Theme = Theme.AUTO,
    val fontType: FontType = FontType.YUSEI_MAGIC,
    val enableInAppUpdate: Boolean = false,
)
