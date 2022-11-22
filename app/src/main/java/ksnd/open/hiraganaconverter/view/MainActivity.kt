package ksnd.open.hiraganaconverter.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ksnd.open.hiraganaconverter.model.PreferenceKeys
import ksnd.open.hiraganaconverter.view.screen.ConverterScreen
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val tag = MainActivity::class.java.simpleName

    @Inject lateinit var preferencesDataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeNum: State<Int> = preferencesDataStore.data
                .catch { exception ->
                    Log.e(tag, "preferencesDataStore $exception")
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        ThemeNum.AUTO.num
                    }
                }
                .map { preferences ->
                    preferences[PreferenceKeys.THEME_NUM] ?: ThemeNum.AUTO.num
                }
                .collectAsState(initial = ThemeNum.AUTO.num)

            val customFont: State<String> = preferencesDataStore.data
                .catch { exception ->
                    Log.e(tag, "preferencesDataStore $exception")
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        CustomFont.DEFAULT.name
                    }
                }
                .map { preferences ->
                    preferences[PreferenceKeys.CUSTOM_FONT] ?: CustomFont.DEFAULT.name
                }
                .collectAsState(initial = CustomFont.DEFAULT.name)

            HiraganaConverterTheme(
                isDarkTheme = when (themeNum.value) {
                    ThemeNum.NIGHT.num -> true
                    ThemeNum.DAY.num -> false
                    else -> isSystemInDarkTheme()
                },
                customFont = customFont.value
            ) {
                ConverterScreen()
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        // 起動時に言語を設定する
        val sharedPreferences = base.getSharedPreferences("DataStore", MODE_PRIVATE)
        val language = sharedPreferences.getString("language", null)
        if (language != null) {
            val config = base.resources.configuration
            config.setLocale(Locale(language))
            super.attachBaseContext(base.createConfigurationContext(config))
        } else {
            super.attachBaseContext(base)
        }
    }
}
