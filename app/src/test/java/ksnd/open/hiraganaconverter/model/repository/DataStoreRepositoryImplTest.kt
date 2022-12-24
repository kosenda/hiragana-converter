package ksnd.open.hiraganaconverter.model.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ksnd.open.hiraganaconverter.R
import ksnd.open.hiraganaconverter.model.TimeFormat
import ksnd.open.hiraganaconverter.model.getNowTime
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStoreRepositoryImplTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("TestDataStore") },
    )
    private val dataStoreRepository = DataStoreRepositoryImpl(
        preferencesDataStore = dataStore,
        ioDispatcher = testDispatcher,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    // ● selectedThemeNum ----------------------------------------------------------------------- ●
    @Test
    fun dataStoreRepository_initialThemeNum_isAutoNum() {
        // テーマの初期値はAUTOであることを確認
        testScope.runTest {
            assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(ThemeNum.AUTO.num)
        }
    }

    // ● selectedCustomFont --------------------------------------------------------------------- ●
    @Test
    fun dataStoreRepository_initialCustomFont_isDefault() {
        // テーマの初期値はAUTOであることを確認
        testScope.runTest {
            assertThat(dataStoreRepository.selectedCustomFont().first())
                .isEqualTo(CustomFont.DEFAULT.name)
        }
    }

    // ● updateThemeNum ------------------------------------------------------------------------- ●
    @Test
    fun dataStoreRepository_updateThemeNum_isChanged() {
        // テーマの更新ができることを確認
        testScope.runTest {
            dataStoreRepository.updateThemeNum(ThemeNum.DAY.num)
            assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(ThemeNum.DAY.num)
            dataStoreRepository.updateThemeNum(ThemeNum.NIGHT.num)
            assertThat(dataStoreRepository.selectedThemeNum().first()).isEqualTo(ThemeNum.NIGHT.num)
        }
    }

    // ● updateCustomFont ----------------------------------------------------------------------- ●
    @Test
    fun dataStoreRepository_updateCustomFont_isChanged() {
        // フォントの更新ができることを確認
        testScope.runTest {
            dataStoreRepository.updateCustomFont(CustomFont.BIZ_UDGOTHIC)
            assertThat(dataStoreRepository.selectedCustomFont().first())
                .isEqualTo(CustomFont.BIZ_UDGOTHIC.name)
            dataStoreRepository.updateCustomFont(CustomFont.CORPORATE_LOGO_ROUNDED)
            assertThat(dataStoreRepository.selectedCustomFont().first())
                .isEqualTo(CustomFont.CORPORATE_LOGO_ROUNDED.name)
        }
    }

    // ● checkReachedConvertMaxLimit ------------------------------------------------------------ ●
    @Test
    fun dataStoreRepository_checkReachedConvertMaxLimitOnce_isFalse() {
        val today = getNowTime(
            timeZone = context.getString(R.string.time_zone),
            format = TimeFormat.YEAR_MONTH_DATE,
        )
        testScope.runTest {
            assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isFalse()
        }
    }

    @Test
    fun dataStoreRepository_checkReachedConvertMaxLimitMaxPlus1Times_isFalse() {
        val today = getNowTime(
            timeZone = context.getString(R.string.time_zone),
            format = TimeFormat.YEAR_MONTH_DATE,
        )
        testScope.runTest {
            repeat(LIMIT_CONVERT_COUNT) {
                assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isFalse()
            }
            repeat(2) {
                assertThat(dataStoreRepository.checkReachedConvertMaxLimit(today)).isTrue()
            }
        }
    }
}
