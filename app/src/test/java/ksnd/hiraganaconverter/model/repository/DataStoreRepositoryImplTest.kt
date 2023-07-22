package ksnd.hiraganaconverter.model.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.model.TimeFormat
import ksnd.hiraganaconverter.model.getNowTime
import ksnd.hiraganaconverter.view.CustomFont
import ksnd.hiraganaconverter.view.Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataStoreRepositoryImplTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("TestDataStore") },
    )
    private val dataStoreRepository = DataStoreRepositoryImpl(
        dataStore = dataStore,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun selectedThemeNum_initial_isAutoNum() = runTest {
        assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(Theme.AUTO.num)
    }

    @Test
    fun selectedCustomFont_initial_isDefault() = runTest {
        assertThat(dataStoreRepository.selectedCustomFont().first()).isEqualTo(CustomFont.DEFAULT.name)
    }

    @Test
    fun updateThemeNum_newThemeNum_isChangedTheme() = runTest {
        dataStoreRepository.updateTheme(Theme.DAY.num)
        assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(Theme.DAY.num)
        dataStoreRepository.updateTheme(Theme.NIGHT.num)
        assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(Theme.NIGHT.num)
    }

    @Test
    fun updateCustomFont_newCustomFont_isChangedCustomFont() = runTest {
        dataStoreRepository.updateCustomFont(CustomFont.BIZ_UDGOTHIC)
        assertThat(dataStoreRepository.selectedCustomFont().first()).isEqualTo(CustomFont.BIZ_UDGOTHIC.name)
        dataStoreRepository.updateCustomFont(CustomFont.CORPORATE_LOGO_ROUNDED)
        assertThat(dataStoreRepository.selectedCustomFont().first()).isEqualTo(CustomFont.CORPORATE_LOGO_ROUNDED.name)
    }

    @Test
    fun checkReachedConvertMaxLimit_first_isFalse() = runTest {
        val today = getNowTime(timeZone = context.getString(R.string.time_zone), format = TimeFormat.YEAR_MONTH_DATE)
        assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isFalse()
    }

    @Test
    fun checkReachedConvertMaxLimit_convertToReachedConvertMaxLimitPlus1_isTrue() = runTest {
        val today = getNowTime(timeZone = context.getString(R.string.time_zone), format = TimeFormat.YEAR_MONTH_DATE)
        repeat(LIMIT_CONVERT_COUNT) { assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isFalse() }
        assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isTrue()
    }
}
