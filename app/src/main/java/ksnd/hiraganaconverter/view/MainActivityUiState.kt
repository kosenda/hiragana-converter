package ksnd.hiraganaconverter.view

import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme

data class MainActivityUiState(
    val theme: Theme = Theme.AUTO,
    val fontType: FontType = FontType.YUSEI_MAGIC,
)