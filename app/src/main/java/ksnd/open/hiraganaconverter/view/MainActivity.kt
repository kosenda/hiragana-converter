package ksnd.open.hiraganaconverter.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ksnd.open.hiraganaconverter.view.screen.ConverterScreen
import ksnd.open.hiraganaconverter.view.theme.HiraganaConverterTheme
import ksnd.open.hiraganaconverter.viewmodel.MainViewModel
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()

            val themeNum: State<Int> =
                mainViewModel.themeNum.collectAsState(initial = ThemeNum.AUTO.num)
            val customFont: State<String> =
                mainViewModel.customFont.collectAsState(initial = CustomFont.DEFAULT.name)

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
