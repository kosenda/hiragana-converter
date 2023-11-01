package ksnd.hiraganaconverter.core.analytics

/**
 * Analytics for Mock (no-op)
 */
class MockAnalytics : Analytics {
    override fun logScreen(screen: Screen) {}
    override fun logConvert(hiraKanaType: String) {}
    override fun logConvertError(error: String) {}
    override fun logChangeHiraKanaType(hiraKanaType: String) {}
    override fun logClearAllText() {}
}
