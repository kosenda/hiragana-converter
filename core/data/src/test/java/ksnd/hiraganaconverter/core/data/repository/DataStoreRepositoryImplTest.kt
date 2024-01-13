package ksnd.hiraganaconverter.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.resource.LIMIT_CONVERT_COUNT
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
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
    )

    @Test
    fun updateTheme_newTheme_isChangedTheme() = runTest {
        dataStoreRepository.updateTheme(Theme.DAY)

        assertThat(dataStoreRepository.theme().first()).isEqualTo(Theme.DAY)

        dataStoreRepository.updateTheme(Theme.NIGHT)

        assertThat(dataStoreRepository.theme().first()).isEqualTo(Theme.NIGHT)
    }

    @Test
    fun updateFontType_newFontType_isChangedFontType() = runTest {
        dataStoreRepository.updateFontType(FontType.ROCKN_ROLL_ONE)

        assertThat(dataStoreRepository.fontType().first()).isEqualTo(FontType.ROCKN_ROLL_ONE)

        dataStoreRepository.updateFontType(FontType.DEFAULT)

        assertThat(dataStoreRepository.fontType().first()).isEqualTo(FontType.DEFAULT)
    }

    @Test
    fun checkIsExceedingMaxLimit_convertToReachedConvertMaxLimitPlus1_isTrue() = runTest {
        repeat(LIMIT_CONVERT_COUNT) { assertThat(dataStoreRepository.checkIsExceedingMaxLimit()).isFalse() }
        assertThat(dataStoreRepository.checkIsExceedingMaxLimit()).isTrue()
    }

    @Test
    fun updateUseInAppUpdate_false_isFalse() = runTest {
        assertThat(dataStoreRepository.enableInAppUpdate().first()).isTrue()

        dataStoreRepository.updateUseInAppUpdate(false)

        assertThat(dataStoreRepository.enableInAppUpdate().first()).isFalse()
    }
}
