package ksnd.open.hiraganaconverter.model.repository

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferencesRepository {

    private val tag = SharedPreferencesRepositoryImpl::class.java.simpleName

    override fun updateSelectLanguage(newLanguage: String) {
        sharedPreferences.edit().putString("language", newLanguage).apply()
        Log.i(tag, "newLanguage: $newLanguage")
    }
}
