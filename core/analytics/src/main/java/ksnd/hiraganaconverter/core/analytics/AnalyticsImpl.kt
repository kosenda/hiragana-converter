package ksnd.hiraganaconverter.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class AnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : Analytics {
    override fun logScreen(screen: Screen) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screen.name)
        }
    }

    override fun logConvert(type: ConvertType) {
        firebaseAnalytics.logEvent(Event.CONVERT.name) {
            param(Param.CONVERT_TYPE.name, type.name)
        }
    }
}
