package ksnd.hiraganaconverter.core.analytics

/**
 * Analytics for Mock (no-op)
 */
class MockAnalytics : Analytics {
    override fun logScreen(screen: Screen) {}
    override fun logConvert(hiraKanaType: String, inputTextLength: Int) {}
    override fun logConvertError(error: String) {}
    override fun logChangeHiraKanaType(hiraKanaType: String) {}
    override fun logClearAllText() {}
    override fun logUpdateTheme(theme: String) {}
    override fun logUpdateLanguage(language: String) {}
    override fun logUpdateFont(font: String) {}
    override fun logSwitchEnableInAppUpdate(isEnable: Boolean) {}
    override fun logTotalConvertCount(count: Int) {}
}
