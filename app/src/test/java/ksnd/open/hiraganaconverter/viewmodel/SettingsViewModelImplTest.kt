package ksnd.open.hiraganaconverter.viewmodel

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepositoryImpl
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelImplTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val viewModel = SettingsViewModelImpl(
        dataStoreRepository = DataStoreRepositoryImpl(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = {
                    context.dataStoreFile("DataStore")
                }
            )
        )
    )

    @Test
    fun settingViewModel_Initialization_FirstDefaultSet() {
        // モードの初期設定は自動モード
        assertTrue(viewModel.isSelectedThemeNum(ThemeNum.AUTO.num))
        // フォントの初期設定はデフォルト
        assertTrue(viewModel.isSelectedFont(CustomFont.DEFAULT))
    }

    @Test
    fun settingViewModel_SettingNightMode_NotAuto() {
        // ダークモードを設定したときにデフォルトでないことを確認
        viewModel.updateThemeNum(ThemeNum.NIGHT.num)
        assertTrue(viewModel.isSelectedThemeNum(ThemeNum.NIGHT.num))
        assertFalse(viewModel.isSelectedThemeNum(ThemeNum.AUTO.num))
    }

    @Test
    fun settingViewModel_SettingCustomFont_NotDefault() {
        // カスタムフォントを設定したときにデフォルトでないことを確認
        viewModel.updateCustomFont(CustomFont.BIZ_UDGOTHIC)
        assertTrue(viewModel.isSelectedFont(CustomFont.BIZ_UDGOTHIC))
        assertFalse(viewModel.isSelectedFont(CustomFont.DEFAULT))
    }
}
