package ksnd.hiraganaconverter.feature.setting

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.analytics.MockAnalytics
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = mockk<DataStoreRepository>(relaxed = true)
    private val analytics = spyk(MockAnalytics())
    private lateinit var viewModel : SettingsViewModel

    @Before
    fun setUp() {
        // Test with different default values for UiState and initial values for the test.
        assertThat(FIRST_SAVED_THEME).isNotEqualTo(SettingsUiState().theme)
        assertThat(FIRST_SAVED_FONT_TYPE).isNotEqualTo(SettingsUiState().fontType)
        assertThat(FIRST_SAVED_ENABLE_IN_APP_UPDATE).isNotEqualTo(SettingsUiState().enableInAppUpdate)

        every { dataStoreRepository.theme() } returns flowOf(FIRST_SAVED_THEME)
        every { dataStoreRepository.fontType() } returns flowOf(FIRST_SAVED_FONT_TYPE)
        every { dataStoreRepository.enableInAppUpdate() } returns flowOf(FIRST_SAVED_ENABLE_IN_APP_UPDATE)
        viewModel = SettingsViewModel(
            dataStoreRepository = dataStoreRepository,
            analytics = analytics,
        )
    }

    @Test
    fun uiState_initial_settingUiState() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.theme).isEqualTo(FIRST_SAVED_THEME)
            assertThat(uiState.fontType).isEqualTo(FIRST_SAVED_FONT_TYPE)
            assertThat(uiState.enableInAppUpdate).isEqualTo(FIRST_SAVED_ENABLE_IN_APP_UPDATE)
        }
        coVerify(exactly = 1) { dataStoreRepository.theme() }
        coVerify(exactly = 1) { dataStoreRepository.fontType() }
        coVerify(exactly = 1) { dataStoreRepository.enableInAppUpdate() }
    }

    @Test
    fun updateTheme_newTheme_isCalledUpdateTheme() = runTest {
        val newTheme = Theme.DAY
        viewModel.updateTheme(newTheme)
        coVerify(exactly = 1) { dataStoreRepository.updateTheme(newTheme) }
        verify(exactly = 1) { analytics.logUpdateTheme(newTheme.name) }
    }

    @Test
    fun updateFontType_newFontType_isCalledUpdateFontType() = runTest {
        val newFontType = FontType.DELA_GOTHIC_ONE
        viewModel.updateFontType(newFontType)
        coVerify(exactly = 1) { dataStoreRepository.updateFontType(newFontType) }
        verify(exactly = 1) { analytics.logUpdateFont(newFontType.name) }
    }

    @Test
    fun updateUseInAppUpdate_false_isCalledUpdateUseInAppUpdate() = runTest {
        val isEnabled = false
        viewModel.updateEnableInAppUpdate(isEnabled)
        coVerify(exactly = 1) { dataStoreRepository.updateUseInAppUpdate(isEnabled) }
        verify(exactly = 1) { analytics.logSwitchEnableInAppUpdate(isEnabled) }
    }

    companion object {
        private val FIRST_SAVED_THEME = Theme.NIGHT
        private val FIRST_SAVED_FONT_TYPE = FontType.HACHI_MARU_POP
        private const val FIRST_SAVED_ENABLE_IN_APP_UPDATE = true
    }
}
