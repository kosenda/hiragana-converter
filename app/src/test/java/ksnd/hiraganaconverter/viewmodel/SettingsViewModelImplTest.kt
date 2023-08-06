package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.model.repository.DataStoreRepositoryImpl
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = mockk<DataStoreRepositoryImpl>(relaxed = true)
    private val viewModel = SettingsViewModelImpl(
        dataStoreRepository = dataStoreRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun isSelected_initial_themeIsAuto() = runTest {
        val theme = viewModel.theme.first()
        Theme.values().forEach {
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
        FontType.values().forEach {
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
        coEvery { dataStoreRepository.updateTheme(newTheme) }
    }

    @Test
    fun updateFontType_newFontType_isCalledUpdateFontType() = runTest {
        val newFontType = FontType.HACHI_MARU_POP
        viewModel.updateFontType(newFontType)
        coEvery { dataStoreRepository.updateFontType(newFontType) }
    }
}
