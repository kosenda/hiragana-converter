package ksnd.hiraganaconverter.core.analytics

interface Analytics {
    fun logScreen(screen: Screen)
    fun logConvert(hiraKanaType: String)
    fun logConvertError(error: String)
    fun logChangeHiraKanaType(hiraKanaType: String)
    fun logClearAllText()
}
