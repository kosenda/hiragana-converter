package ksnd.hiraganaconverter.core.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val THEME_NUM = intPreferencesKey("theme_num")
    val FONT_TYPE = stringPreferencesKey("font_type")
    val LAST_CONVERT_DATE = stringPreferencesKey("last_convert_date")
    val TODAY_CONVERT_COUNT = intPreferencesKey("today_convert_count")
    val TOTAL_CONVERT_COUNT = intPreferencesKey("total_convert_count")
    val ENABLE_IN_APP_UPDATE = booleanPreferencesKey("enable_in_app_update")
}
