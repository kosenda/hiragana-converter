package ksnd.hiraganaconverter.core.analytics

interface AnalyticsHelper {
    fun logScreen(screen: Screen)
    fun logEvent(analyticsEvent: AnalyticsEvent)
}
