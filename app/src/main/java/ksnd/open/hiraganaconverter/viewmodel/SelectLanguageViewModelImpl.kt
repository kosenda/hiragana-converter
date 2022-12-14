package ksnd.open.hiraganaconverter.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ksnd.open.hiraganaconverter.model.repository.SharedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SelectLanguageViewModelImpl @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : SelectLanguageViewModel() {

    override fun updateSelectLanguage(newLanguage: String) {
        sharedPreferencesRepository.updateSelectLanguage(newLanguage)
    }
}
