package ksnd.hiraganaconverter.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import timber.log.Timber
import javax.inject.Inject

class AnalyticsHelperImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsHelper {
    override fun logScreen(screen: Screen) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            Timber.d("logScreen: screen=${screen.name}")
            param(FirebaseAnalytics.Param.SCREEN_NAME, screen.name)
        }
    }

    override fun logEvent(analyticsEvent: AnalyticsEvent) {
        firebaseAnalytics.logEvent(analyticsEvent.event.name) {
            Timber.d("logEvent: event=${analyticsEvent.event}, params=${analyticsEvent.params}")
            analyticsEvent.params.forEach { param(it.param.name, it.value) }
        }
    }
}
