package ksnd.open.hiraganaconverter.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ksnd.open.hiraganaconverter.model.PreferenceKeys
import ksnd.open.hiraganaconverter.view.CustomFont
import ksnd.open.hiraganaconverter.view.ThemeNum
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) : SettingsViewModel() {

    private val _themeNum = mutableStateOf(ThemeNum.AUTO.num)
    override val themeNum: Int = _themeNum.value
    private val _customFont = mutableStateOf(CustomFont.DEFAULT.name)
    override val customFont = _customFont.value

    override fun updateThemeNum(newThemeNum: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesDataStore.edit { it[PreferenceKeys.THEME_NUM] = newThemeNum }
            _themeNum.value = newThemeNum
        }
    }

    override fun updateCustomFont(newCustomFont: CustomFont) {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesDataStore.edit { it[PreferenceKeys.CUSTOM_FONT] = newCustomFont.name }
            _customFont.value = newCustomFont.name
        }
    }

    override fun getThemeNum() {
        CoroutineScope(Dispatchers.IO).launch {
            val customFontFlow: Flow<Int> = preferencesDataStore.data
                .catch { exception ->
                    Log.e("preferencesDataStore", exception.toString())
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        ThemeNum.AUTO.num
                    }
                }
                .map { preferences ->
                    preferences[PreferenceKeys.THEME_NUM] ?: ThemeNum.AUTO.num
                }
            customFontFlow.collect {
                _themeNum.value = it
            }
        }
    }

    override fun getCustomFont() {
        CoroutineScope(Dispatchers.IO).launch {
            val customFontFlow: Flow<String> = preferencesDataStore.data
                .catch { exception ->
                    Log.e("preferencesDataStore", exception.toString())
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        CustomFont.DEFAULT.name
                    }
                }
                .map { preferences ->
                    preferences[PreferenceKeys.CUSTOM_FONT] ?: CustomFont.DEFAULT.name
                }
            customFontFlow.collect {
                _customFont.value = it
            }
        }
    }

    override fun isSelectedThemeNum(index: Int): Boolean {
        return themeNum == index
    }

    override fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return customFont == targetCustomFont.name
    }
}
