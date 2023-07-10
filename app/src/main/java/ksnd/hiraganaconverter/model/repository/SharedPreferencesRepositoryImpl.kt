package ksnd.hiraganaconverter.model.repository

import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferencesRepository {

    override fun updateSelectLanguage(newLanguage: String) {
        sharedPreferences.edit().putString("language", newLanguage).apply()
        Timber.i("newLanguage: %s", newLanguage)
    }
}
