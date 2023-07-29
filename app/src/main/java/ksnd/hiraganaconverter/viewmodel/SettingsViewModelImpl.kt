package ksnd.hiraganaconverter.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    override val theme = mutableIntStateOf(Theme.AUTO.num)
    override val fontType = mutableStateOf(FontType.YUSEI_MAGIC)

    private val fontTypeFlow: StateFlow<String> = dataStoreRepository
        .selectedFontType()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FontType.YUSEI_MAGIC.name,
        )

    private val themeFlow: StateFlow<Int> = dataStoreRepository
        .selectedTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Theme.AUTO.num,
        )

    init {
        theme.intValue = themeFlow.value
        FontType.values().forEach { if (fontTypeFlow.value == it.fontName) fontType.value = it }
    }

    override fun updateTheme(newTheme: Int) {
        theme.intValue = newTheme
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateTheme(newTheme)
        }
    }

    override fun updateFontType(newFontType: FontType) {
        fontType.value = newFontType
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateCustomFont(newFontType)
        }
    }

    override fun isSelectedTheme(index: Int): Boolean {
        return theme.intValue == index
    }

    override fun isSelectedFontType(targetFontType: FontType): Boolean {
        return fontType.value == targetFontType
    }
}
