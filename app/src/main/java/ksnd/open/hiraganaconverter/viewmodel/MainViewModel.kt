package ksnd.open.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val themeNum: Flow<Int> = dataStoreRepository.selectedThemeNum()
    val customFont: Flow<String> = dataStoreRepository.selectedCustomFont()
}
