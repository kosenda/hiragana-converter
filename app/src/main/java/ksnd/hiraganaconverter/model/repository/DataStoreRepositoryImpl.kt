package ksnd.hiraganaconverter.model.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ksnd.hiraganaconverter.di.module.IODispatcher
import ksnd.hiraganaconverter.model.PreferenceKeys
import ksnd.hiraganaconverter.view.CustomFont
import ksnd.hiraganaconverter.view.ThemeNum
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

const val LIMIT_CONVERT_COUNT = 200

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : DataStoreRepository {
    override fun selectedThemeNum(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> ThemeNum.AUTO.num
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.THEME_NUM] ?: ThemeNum.AUTO.num
            }
    }

    override fun selectedCustomFont(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> CustomFont.DEFAULT.name
                }
            }
            .map { preferences ->
                preferences[PreferenceKeys.CUSTOM_FONT] ?: CustomFont.DEFAULT.name
            }
    }

    override suspend fun updateThemeNum(newThemeNum: Int) {
        dataStore.edit { it[PreferenceKeys.THEME_NUM] = newThemeNum }
    }

    override suspend fun updateCustomFont(newCustomFont: CustomFont) {
        dataStore.edit { it[PreferenceKeys.CUSTOM_FONT] = newCustomFont.name }
    }

    override suspend fun checkReachedConvertMaxLimit(today: String): Boolean {
        val oldConvertCount: Int = try {
            convertCount().first()
        } catch (e: NoSuchElementException) {
            0
        }
        val oldLastConvertTime: String = try {
            lastConvertTime().first()
        } catch (e: NoSuchElementException) {
            ""
        }
        Timber.i("old_convert_count: %d", oldConvertCount)
        Timber.i("old_convert_time: %s", oldLastConvertTime)

        return if (today != oldLastConvertTime) {
            updateLastConvertTime(today)
            updateConvertCount(1)
            Timber.i("new_convert_count: 1")
            Timber.i("new_convert_time: %s", today)
            false
        } else {
            val newConvertCount = oldConvertCount + 1
            Timber.i("new_convert_count: %d", newConvertCount)
            updateConvertCount(newConvertCount)
            newConvertCount > LIMIT_CONVERT_COUNT
        }
    }

    private fun lastConvertTime(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                if (exception is IOException) emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PreferenceKeys.LAST_CONVERT_TIME] ?: ""
            }
    }

    private fun convertCount(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                if (exception is IOException) emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PreferenceKeys.CONVERT_COUNT] ?: 1
            }
    }

    private suspend fun updateLastConvertTime(lastConvertTime: String) = withContext(ioDispatcher) {
        dataStore.edit { it[PreferenceKeys.LAST_CONVERT_TIME] = lastConvertTime }
    }

    private suspend fun updateConvertCount(convertCount: Int) = withContext(ioDispatcher) {
        dataStore.edit { it[PreferenceKeys.CONVERT_COUNT] = convertCount }
    }
}
