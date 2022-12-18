package ksnd.open.hiraganaconverter.model.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.di.module.IODispatcher
import ksnd.open.hiraganaconverter.model.PreferenceKeys
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import java.io.IOException
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : DataStoreRepository {

    private val tag = DataStoreRepositoryImpl::class.java.simpleName

    override fun selectedThemeNum(): Flow<Int> {
        return preferencesDataStore.data
            .catch { exception ->
                Log.e(tag, "preferencesDataStore $exception")
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    ThemeNum.AUTO.num
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.THEME_NUM] ?: ThemeNum.AUTO.num
            }
    }

    override fun selectedCustomFont(): Flow<String> {
        return preferencesDataStore.data
            .catch { exception ->
                Log.e(tag, "preferencesDataStore $exception")
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    CustomFont.DEFAULT.name
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.CUSTOM_FONT] ?: CustomFont.DEFAULT.name
            }
    }

    override fun updateThemeNum(newThemeNum: Int) {
        CoroutineScope(ioDispatcher).launch {
            preferencesDataStore.edit { it[PreferenceKeys.THEME_NUM] = newThemeNum }
        }
    }

    override fun updateCustomFont(newCustomFont: CustomFont) {
        CoroutineScope(ioDispatcher).launch {
            preferencesDataStore.edit { it[PreferenceKeys.CUSTOM_FONT] = newCustomFont.name }
        }
    }

    override fun lastConvertTime(): Flow<String> {
        return preferencesDataStore.data
            .catch { exception ->
                Log.e(tag, "preferencesDataStore $exception")
                if (exception is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.LAST_CONVERT_TIME] ?: ""
            }
    }

    override fun convertCount(): Flow<Int> {
        return preferencesDataStore.data
            .catch { exception ->
                Log.e(tag, "preferencesDataStore $exception")
                if (exception is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.CONVERT_COUNT] ?: 1
            }
    }

    override fun updateLastConvertTime(lastConvertTime: String) {
        CoroutineScope(ioDispatcher).launch {
            preferencesDataStore.edit { it[PreferenceKeys.LAST_CONVERT_TIME] = lastConvertTime }
        }
    }

    override fun updateConvertCount(convertCount: Int) {
        CoroutineScope(ioDispatcher).launch {
            preferencesDataStore.edit { it[PreferenceKeys.CONVERT_COUNT] = convertCount }
        }
    }
}
