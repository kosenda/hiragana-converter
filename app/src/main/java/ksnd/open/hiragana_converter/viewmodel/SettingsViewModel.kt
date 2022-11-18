package ksnd.open.hiragana_converter.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ksnd.open.hiragana_converter.model.PreferenceKeys
import ksnd.open.hiragana_converter.view.CustomFont
import ksnd.open.hiragana_converter.view.ThemeNum
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
): ViewModel() {

    private val customFont = mutableStateOf(CustomFont.DEFAULT.name)
    private val themeNum   = mutableStateOf(ThemeNum.AUTO.num)

    fun updateThemeNum(newThemeNum: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesDataStore.edit { it[PreferenceKeys.THEME_NUM] = newThemeNum }
            themeNum.value = newThemeNum
        }
    }

    fun updateCustomFont(newCustomFont: CustomFont) {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesDataStore.edit { it[PreferenceKeys.CUSTOM_FONT] = newCustomFont.name }
            customFont.value = newCustomFont.name
        }
    }

    fun getThemeNum() {
        CoroutineScope(Dispatchers.IO).launch {
            val customFontFlow: Flow<Int> = preferencesDataStore.data
                .catch { exception ->
                    Log.e("preferencesDataStore", exception.toString())
                    if(exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        ThemeNum.AUTO.num
                    } }
                .map { preferences ->
                    preferences[PreferenceKeys.THEME_NUM] ?: ThemeNum.AUTO.num
                }
            customFontFlow.collect {
                themeNum.value = it
            }
        }
    }

    fun getCustomFont() {
        CoroutineScope(Dispatchers.IO).launch {
            val customFontFlow: Flow<String> = preferencesDataStore.data
                .catch { exception ->
                    Log.e("preferencesDataStore", exception.toString())
                    if(exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        CustomFont.DEFAULT.name
                    } }
                .map { preferences ->
                   preferences[PreferenceKeys.CUSTOM_FONT] ?: CustomFont.DEFAULT.name
                }
            customFontFlow.collect {
                customFont.value = it
            }
        }
    }

    fun isSelectedThemeNum(index: Int): Boolean {
        return themeNum.value == index
    }

    fun isSelectedFont(targetCustomFont: CustomFont): Boolean {
        return customFont.value == targetCustomFont.name
    }
}