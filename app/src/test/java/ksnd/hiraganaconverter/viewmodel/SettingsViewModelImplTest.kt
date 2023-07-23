package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.model.repository.DataStoreRepositoryImpl
import ksnd.hiraganaconverter.view.CustomFont
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
    fun isSelectedThemeNum_initial_themeIsAuto() {
        Theme.values().forEach {
            if (it == Theme.AUTO) {
                assertThat(viewModel.isSelectedThemeNum(it.num)).isTrue()
            } else {
                assertThat(viewModel.isSelectedThemeNum(it.num)).isFalse()
            }
        }
    }

    @Test
    fun isSelectedCustomFont_initial_customFontIsDefault() {
        CustomFont.values().forEach {
            if (it == CustomFont.DEFAULT) {
                assertThat(viewModel.isSelectedFont(it)).isTrue()
            } else {
                assertThat(viewModel.isSelectedFont(it)).isFalse()
            }
        }
    }

    @Test
    fun updateThemeNum_newThemeNum_isChanged() = runTest {
        assertThat(viewModel.isSelectedThemeNum(Theme.AUTO.num)).isTrue()
        viewModel.updateThemeNum(Theme.NIGHT.num)
        assertThat(viewModel.isSelectedThemeNum(Theme.NIGHT.num)).isTrue()
        viewModel.updateThemeNum(Theme.DAY.num)
        assertThat(viewModel.isSelectedThemeNum(Theme.DAY.num)).isTrue()
    }

    @Test
    fun updateCustomFont_newCustomFont_isChanged() = runTest {
        assertThat(viewModel.isSelectedFont(CustomFont.DEFAULT)).isTrue()
        viewModel.updateCustomFont(CustomFont.BIZ_UDGOTHIC)
        assertThat(viewModel.isSelectedFont(CustomFont.BIZ_UDGOTHIC)).isTrue()
        viewModel.updateCustomFont(CustomFont.CORPORATE_LOGO_ROUNDED)
        assertThat(viewModel.isSelectedFont(CustomFont.CORPORATE_LOGO_ROUNDED)).isTrue()
    }
}
