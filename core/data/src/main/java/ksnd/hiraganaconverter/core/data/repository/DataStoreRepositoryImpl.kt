package ksnd.hiraganaconverter.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ksnd.hiraganaconverter.core.data.PreferenceKeys
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.LIMIT_CONVERT_COUNT
import timber.log.Timber
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {
    override fun theme(): Flow<Theme> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "DataStore: %s", exception.message)
            if (exception is IOException) emit(emptyPreferences()) else Theme.AUTO
        }
        .map { preferences ->
            Theme.entries.firstOrNull { it.num == preferences[PreferenceKeys.THEME_NUM] } ?: Theme.AUTO
        }

    override fun fontType(): Flow<FontType> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "DataStore: %s", exception.message)
            if (exception is IOException) emit(emptyPreferences()) else FontType.YUSEI_MAGIC
        }
        .map { preferences ->
            FontType.entries.firstOrNull { it.fontName == preferences[PreferenceKeys.FONT_TYPE] } ?: FontType.YUSEI_MAGIC
        }

    private fun lastConvertTime(): Flow<LocalDate?> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "DataStore: %s", exception.message)
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferenceKeys.LAST_CONVERT_DATE]?.let { LocalDate.parse(it) }
        }

    private fun todayConvertCount(): Flow<Int> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "DataStore: %s", exception.message)
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferenceKeys.TODAY_CONVERT_COUNT] ?: 1
        }

    override fun enableInAppUpdate(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "DataStore: %s", exception.message)
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferenceKeys.ENABLE_IN_APP_UPDATE] ?: true
        }

    override suspend fun updateTheme(newTheme: Theme) {
        dataStore.edit { it[PreferenceKeys.THEME_NUM] = newTheme.num }
    }

    override suspend fun updateFontType(fontType: FontType) {
        dataStore.edit { it[PreferenceKeys.FONT_TYPE] = fontType.fontName }
    }

    override suspend fun checkIsExceedingMaxLimit(): Boolean {
        val today = LocalDate.now()
        return if (today == lastConvertTime().firstOrNull()) {
            val newConvertCount = (todayConvertCount().firstOrNull() ?: 0) + 1
            updateTodayConvertCount(newConvertCount)
            newConvertCount > LIMIT_CONVERT_COUNT
        } else {
            updateLastConvertTime(today)
            updateTodayConvertCount(1)
            false
        }
    }

    override suspend fun updateUseInAppUpdate(isUsed: Boolean) {
        dataStore.edit { it[PreferenceKeys.ENABLE_IN_APP_UPDATE] = isUsed }
    }

    private suspend fun updateLastConvertTime(convertDate: LocalDate) {
        dataStore.edit { it[PreferenceKeys.LAST_CONVERT_DATE] = convertDate.toString() }
    }

    private suspend fun updateTodayConvertCount(convertCount: Int) {
        dataStore.edit { it[PreferenceKeys.TODAY_CONVERT_COUNT] = convertCount }
    }
}
