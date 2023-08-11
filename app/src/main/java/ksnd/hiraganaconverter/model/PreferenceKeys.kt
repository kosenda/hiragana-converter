package ksnd.hiraganaconverter.model

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val THEME_NUM = intPreferencesKey("theme_num")
    val FONT_TYPE = stringPreferencesKey("font_type")
    val LAST_CONVERT_DATE = stringPreferencesKey("last_convert_date")
    val CONVERT_COUNT = intPreferencesKey("convert_count")
}
