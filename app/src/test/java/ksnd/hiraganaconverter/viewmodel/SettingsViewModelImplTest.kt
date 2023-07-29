package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
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
    fun isSelected_initial_themeIsAuto() {
        Theme.values().forEach {
            if (it == Theme.AUTO) {
                assertThat(viewModel.isSelectedTheme(it.num)).isTrue()
            } else {
                assertThat(viewModel.isSelectedTheme(it.num)).isFalse()
            }
        }
    }

    @Test
    fun isSelectedFontType_initial_fontTypeIsYuseiMagic() {
        FontType.values().forEach {
            if (it == FontType.YUSEI_MAGIC) {
                assertThat(viewModel.isSelectedFontType(it)).isTrue()
            } else {
                assertThat(viewModel.isSelectedFontType(it)).isFalse()
            }
        }
    }

    @Test
    fun updateTheme_newTheme_isChanged() = runTest {
        assertThat(viewModel.isSelectedTheme(Theme.AUTO.num)).isTrue()
        viewModel.updateTheme(Theme.NIGHT.num)
        assertThat(viewModel.isSelectedTheme(Theme.NIGHT.num)).isTrue()
        viewModel.updateTheme(Theme.DAY.num)
        assertThat(viewModel.isSelectedTheme(Theme.DAY.num)).isTrue()
    }

    @Test
    fun updateFontType_newFontType_isChanged() = runTest {
        assertThat(viewModel.isSelectedFontType(FontType.YUSEI_MAGIC)).isTrue()
        viewModel.updateFontType(FontType.HACHI_MARU_POP)
        assertThat(viewModel.isSelectedFontType(FontType.HACHI_MARU_POP)).isTrue()
        viewModel.updateFontType(FontType.ROCKN_ROLL_ONE)
        assertThat(viewModel.isSelectedFontType(FontType.ROCKN_ROLL_ONE)).isTrue()
    }
}
