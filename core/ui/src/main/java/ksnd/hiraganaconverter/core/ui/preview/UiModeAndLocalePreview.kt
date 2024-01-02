package ksnd.hiraganaconverter.core.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, group = "ui")
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, group = "ui")
annotation class UiModePreview
