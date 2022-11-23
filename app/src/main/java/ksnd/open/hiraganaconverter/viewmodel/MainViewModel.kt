package ksnd.open.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ksnd.open.hiraganaconverter.model.repository.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    themeRepository: ThemeRepository
) : ViewModel() {
    val themeNum: Flow<Int> = themeRepository.selectedThemeNum()
    val customFont: Flow<String> = themeRepository.selectedCustomFont()
}
