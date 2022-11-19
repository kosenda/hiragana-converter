package ksnd.open.hiragana_converter.model

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val THEME_NUM = intPreferencesKey("theme_num")
    val CUSTOM_FONT = stringPreferencesKey("custom_font")
}
