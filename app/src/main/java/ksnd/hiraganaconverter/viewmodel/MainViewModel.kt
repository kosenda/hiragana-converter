package ksnd.hiraganaconverter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    val theme: Flow<Theme> = dataStoreRepository.selectedTheme()
    val fontType: Flow<FontType> = dataStoreRepository.selectedFontType()
}
