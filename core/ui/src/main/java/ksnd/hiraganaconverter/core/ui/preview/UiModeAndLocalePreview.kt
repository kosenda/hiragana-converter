package ksnd.hiraganaconverter.core.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark and en_US", uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "en_US")
@Preview(name = "Dark and ja", uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ja")
@Preview(name = "Light and en_US", uiMode = Configuration.UI_MODE_NIGHT_NO, locale = "en_US")
@Preview(name = "Light and ja", uiMode = Configuration.UI_MODE_NIGHT_NO, locale = "ja")
annotation class UiModeAndLocalePreview

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class UiModePreview
