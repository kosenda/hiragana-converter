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
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

const val LIMIT_CONVERT_COUNT = 200

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : DataStoreRepository {
    override fun selectedTheme(): Flow<Theme> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                if (exception is IOException) emit(emptyPreferences()) else Theme.AUTO
            }
            .map { preferences ->
                Theme.values().firstOrNull { it.num == preferences[PreferenceKeys.THEME_NUM] } ?: Theme.AUTO
            }
    }

    override fun selectedFontType(): Flow<FontType> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s", exception)
                if (exception is IOException) emit(emptyPreferences()) else FontType.YUSEI_MAGIC
            }
            .map { preferences ->
                FontType.values().firstOrNull { it.fontName == preferences[PreferenceKeys.FONT_TYPE] } ?: FontType.YUSEI_MAGIC
            }
    }

    override suspend fun updateTheme(newTheme: Theme) {
        dataStore.edit { it[PreferenceKeys.THEME_NUM] = newTheme.num }
    }

    override suspend fun updateFontType(fontType: FontType) {
        dataStore.edit { it[PreferenceKeys.FONT_TYPE] = fontType.fontName }
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
        Timber.i("old_convert_count: %d".format(oldConvertCount))
        Timber.i("old_convert_time: %s".format(oldLastConvertTime))

        return if (today != oldLastConvertTime) {
            updateLastConvertTime(today)
            updateConvertCount(1)
            Timber.i("new_convert_count: 1")
            Timber.i("new_convert_time: %s".format(today))
            false
        } else {
            val newConvertCount = oldConvertCount + 1
            Timber.i("new_convert_count: %d".format(newConvertCount))
            updateConvertCount(newConvertCount)
            newConvertCount > LIMIT_CONVERT_COUNT
        }
    }

    private fun lastConvertTime(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s".format(exception))
                if (exception is IOException) emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PreferenceKeys.LAST_CONVERT_TIME] ?: ""
            }
    }

    private fun convertCount(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                Timber.e("DataStore: %s".format(exception))
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
