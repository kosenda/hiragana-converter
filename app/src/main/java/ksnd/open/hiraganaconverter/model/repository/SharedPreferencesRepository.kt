package ksnd.open.hiraganaconverter.model.repository

interface SharedPreferencesRepository {
    fun updateSelectLanguage(newLanguage: String)
}
