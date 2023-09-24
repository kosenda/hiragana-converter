package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
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
class SettingsViewModelImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = mockk<DataStoreRepository>(relaxed = true)
    private val viewModel = SettingsViewModelImpl(
        dataStoreRepository = dataStoreRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun isSelected_initial_themeIsAuto() = runTest {
        val theme = viewModel.theme.first()
        Theme.entries.forEach {
            if (it == Theme.AUTO) {
                assertThat(theme).isEqualTo(it)
            } else {
                assertThat(theme).isNotEqualTo(it)
            }
        }
    }

    @Test
    fun isSelectedFontType_initial_fontTypeIsYuseiMagic() = runTest {
        val fontType = viewModel.fontType.first()
        FontType.entries.forEach {
            if (it == FontType.YUSEI_MAGIC) {
                assertThat(fontType).isEqualTo(it)
            } else {
                assertThat(fontType).isNotEqualTo(it)
            }
        }
    }

    @Test
    fun updateTheme_newTheme_isCalledUpdateTheme() = runTest {
        val newTheme = Theme.DAY
        viewModel.updateTheme(newTheme)
        coVerify { dataStoreRepository.updateTheme(newTheme) }
    }

    @Test
    fun updateFontType_newFontType_isCalledUpdateFontType() = runTest {
        val newFontType = FontType.HACHI_MARU_POP
        viewModel.updateFontType(newFontType)
        coVerify { dataStoreRepository.updateFontType(newFontType) }
    }

    @Test
    fun updateUseInAppUpdate_false_isCaledUpdateUseInAppUpdate() = runTest {
        val isEnabled = false
        viewModel.updateUseInAppUpdate(isEnabled)
        coVerify { dataStoreRepository.updateUseInAppUpdate(isEnabled) }
    }
}
