package ksnd.hiraganaconverter.viewmodel

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
import ksnd.hiraganaconverter.view.CustomFont
import ksnd.hiraganaconverter.view.ThemeNum
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : SettingsViewModel() {

    override val themeNum = mutableStateOf(ThemeNum.AUTO.num)
    override val customFont = mutableStateOf(CustomFont.DEFAULT.name)

    private val customFontFlow: StateFlow<String> = dataStoreRepository
        .selectedCustomFont()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CustomFont.DEFAULT.name,
        )

    private val themeNumFlow: StateFlow<Int> = dataStoreRepository
        .selectedThemeNum()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeNum.AUTO.num,
        )

    init {
        themeNum.value = themeNumFlow.value
        customFont.value = customFontFlow.value
    }

    override fun updateThemeNum(newThemeNum: Int) {
        themeNum.value = newThemeNum
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateThemeNum(newThemeNum)
        }
    }

    override fun updateCustomFont(newCustomFont: CustomFont) {
        customFont.value = newCustomFont.name
        CoroutineScope(ioDispatcher).launch {
            dataStoreRepository.updateCustomFont(newCustomFont)
        }
    }

    override fun isSelectedThemeNum(index: Int): Boolean {
        return themeNum.value == index
    }

    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return customFont.value == targetCustomFont.name
    }
}
