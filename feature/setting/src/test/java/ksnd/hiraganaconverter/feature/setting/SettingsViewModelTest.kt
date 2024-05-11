package ksnd.hiraganaconverter.feature.setting

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.analytics.MockAnalyticsHelper
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = mockk<DataStoreRepository>(relaxed = true)
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        every { dataStoreRepository.theme() } returns flowOf(FIRST_SAVED_THEME)
        every { dataStoreRepository.fontType() } returns flowOf(FIRST_SAVED_FONT_TYPE)
        every { dataStoreRepository.enableInAppUpdate() } returns flowOf(FIRST_SAVED_ENABLE_IN_APP_UPDATE)
        viewModel = SettingsViewModel(
            dataStoreRepository = dataStoreRepository,
            analytics = MockAnalyticsHelper(),
        )
    }

    @Test
    fun uiState_initial_callRepositoryMethods() = runTest {
        coVerify(exactly = 1) { dataStoreRepository.theme() }
        coVerify(exactly = 1) { dataStoreRepository.fontType() }
        coVerify(exactly = 1) { dataStoreRepository.enableInAppUpdate() }
    }

    @Test
    fun updateTheme_newTheme_isCalledUpdateTheme() = runTest {
        viewModel.updateTheme(NEW_THEME)

        coVerify(exactly = 1) { dataStoreRepository.updateTheme(newTheme = NEW_THEME) }
    }

    @Test
    fun updateFontType_newFontType_isCalledUpdateFontType() = runTest {
        viewModel.updateFontType(NEW_FONT_TYPE)

        coVerify(exactly = 1) { dataStoreRepository.updateFontType(fontType = NEW_FONT_TYPE) }
    }

    @Test
    fun updateUseInAppUpdate_false_isCalledUpdateUseInAppUpdate() = runTest {
        viewModel.updateEnableInAppUpdate(isEnabled = NEW_ENABLE_IN_APP_UPDATE)

        coVerify(exactly = 1) { dataStoreRepository.updateUseInAppUpdate(NEW_ENABLE_IN_APP_UPDATE) }
    }

    companion object {
        private val FIRST_SAVED_THEME = Theme.NIGHT
        private val NEW_THEME = Theme.DAY
        private val FIRST_SAVED_FONT_TYPE = FontType.HACHI_MARU_POP
        private val NEW_FONT_TYPE = FontType.DELA_GOTHIC_ONE
        private const val FIRST_SAVED_ENABLE_IN_APP_UPDATE = true
        private const val NEW_ENABLE_IN_APP_UPDATE = false
    }
}
