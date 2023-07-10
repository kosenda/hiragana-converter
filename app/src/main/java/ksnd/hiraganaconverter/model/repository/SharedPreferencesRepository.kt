package ksnd.hiraganaconverter.model.repository

interface SharedPreferencesRepository {
    fun updateSelectLanguage(newLanguage: String)
}
