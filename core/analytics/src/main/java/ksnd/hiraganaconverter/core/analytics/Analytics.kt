package ksnd.hiraganaconverter.core.analytics

interface Analytics {
    fun logScreen(screen: Screen)
    fun logConvert(type: ConvertType)
}
