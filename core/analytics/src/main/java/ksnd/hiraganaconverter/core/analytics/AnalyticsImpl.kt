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

    override fun logConvert(hiraKanaType: String, inputTextLength: Int) {
        firebaseAnalytics.logEvent(Event.CONVERT.name) {
            param(Param.CONVERT_TYPE.name, hiraKanaType)
            param(Param.INPUT_TEXT_LENGTH.name, inputTextLength.toString())
        }
    }

    override fun logConvertError(error: String) {
        firebaseAnalytics.logEvent(Event.CONVERT_ERROR.name) {
            param(Param.CONVERT_ERROR.name, error)
        }
    }

    override fun logChangeHiraKanaType(hiraKanaType: String) {
        firebaseAnalytics.logEvent(Event.CHANGE_HIRA_KANA_TYPE.name) {
            param(Param.CONVERT_TYPE.name, hiraKanaType)
        }
    }

    override fun logClearAllText() {
        firebaseAnalytics.logEvent(Event.CLEAR_ALL_TEXT.name) {}
    }

    override fun logUpdateTheme(theme: String) {
        firebaseAnalytics.logEvent(Event.UPDATE_THEME.name) {
            param(Param.THEME.name, theme)
        }
    }

    override fun logUpdateLanguage(language: String) {
        firebaseAnalytics.logEvent(Event.UPDATE_LANGUAGE.name) {
            param(Param.LANGUAGE.name, language)
        }
    }

    override fun logUpdateFont(font: String) {
        firebaseAnalytics.logEvent(Event.UPDATE_FONT.name) {
            param(Param.FONT.name, font)
        }
    }

    override fun logSwitchEnableInAppUpdate(isEnable: Boolean) {
        firebaseAnalytics.logEvent(Event.SWITCH_ENABLE_IN_APP_UPDATE.name) {
            param(Param.IS_ENABLE_IN_APP_UPDATE.name, isEnable.toString())
        }
    }

    override fun logTotalConvertCount(count: Int) {
        firebaseAnalytics.logEvent(Event.COUNT_UP_TOTAL_CONVERT_COUNT.name) {
            param(Param.COUNT.name, count.toString())
        }
    }

    override fun logRequestReview() {
        firebaseAnalytics.logEvent(Event.REQUEST_REVIEW.name) {}
    }
}
