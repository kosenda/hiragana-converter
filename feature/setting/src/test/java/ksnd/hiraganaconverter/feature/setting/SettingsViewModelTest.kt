package ksnd.hiraganaconverter.feature.setting

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = mockk<DataStoreRepository>(relaxed = true)
    private val viewModel = SettingsViewModel(
        dataStoreRepository = dataStoreRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun uiState_isCalledCombine() = runTest {
        coVerify(exactly = 1) { dataStoreRepository.selectedTheme() }
        coVerify(exactly = 1) { dataStoreRepository.selectedFontType() }
        coVerify(exactly = 1) { dataStoreRepository.enableInAppUpdate() }
    }

    @Test
    fun updateTheme_newTheme_isCalledUpdateTheme() = runTest {
        val newTheme = Theme.DAY
        viewModel.updateTheme(newTheme)
        coVerify(exactly = 1) { dataStoreRepository.updateTheme(newTheme) }
    }

    @Test
    fun updateFontType_newFontType_isCalledUpdateFontType() = runTest {
        val newFontType = FontType.HACHI_MARU_POP
        viewModel.updateFontType(newFontType)
        coVerify(exactly = 1) { dataStoreRepository.updateFontType(newFontType) }
    }

    @Test
    fun updateUseInAppUpdate_false_isCaledUpdateUseInAppUpdate() = runTest {
        val isEnabled = false
        viewModel.updateUseInAppUpdate(isEnabled)
        coVerify(exactly = 1) { dataStoreRepository.updateUseInAppUpdate(isEnabled) }
    }
}
