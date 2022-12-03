package ksnd.open.hiraganaconverter.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : SettingsViewModel() {

    override val themeNum = mutableStateOf(ThemeNum.AUTO.num)
    override val customFont = mutableStateOf(CustomFont.DEFAULT.name)

    private val customFontFlow: StateFlow<String> = dataStoreRepository
        .selectedCustomFont()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CustomFont.DEFAULT.name
        )

    private val themeNumFlow: StateFlow<Int> = dataStoreRepository
        .selectedThemeNum()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeNum.AUTO.num
        )

    init {
        getThemeNum()
        getCustomFont()
    }

    override fun updateThemeNum(newThemeNum: Int) {
        themeNum.value = newThemeNum
        dataStoreRepository.updateThemeNum(newThemeNum)
    }

    override fun updateCustomFont(newCustomFont: CustomFont) {
        customFont.value = newCustomFont.name
        dataStoreRepository.updateCustomFont(newCustomFont)
    }

    override fun isSelectedThemeNum(index: Int): Boolean {
        return themeNum.value == index
    }

    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return customFont.value == targetCustomFont.name
    }

    private fun getThemeNum() {
        themeNum.value = themeNumFlow.value
    }

    private fun getCustomFont() {
        customFont.value = customFontFlow.value
    }
}
