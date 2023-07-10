package ksnd.hiraganaconverter.model

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val THEME_NUM = intPreferencesKey("theme_num")
    val CUSTOM_FONT = stringPreferencesKey("custom_font")
    val LAST_CONVERT_TIME = stringPreferencesKey("last_convert_time")
    val CONVERT_COUNT = intPreferencesKey("convert_count")
}
