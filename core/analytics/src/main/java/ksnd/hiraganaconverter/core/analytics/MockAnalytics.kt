package ksnd.hiraganaconverter.core.analytics

/**
 * Analytics for Mock (no-op)
 */
class MockAnalyticsHelper : AnalyticsHelper {
    override fun logScreen(screen: Screen) {}
    override fun logEvent(analyticsEvent: AnalyticsEvent) {}
}
