package ksnd.hiraganaconverter.core.analytics


sealed class AnalyticsEvent {
    abstract val event: Event
    open val params: List<AnalyticsParam> = emptyList()
}

data class AnalyticsParam(
    val param: Param,
    val value: String,
)

class Convert(hiraKanaType: String, inputTextLength: Int) : AnalyticsEvent() {
    override val event = Event.CONVERT
    override val params = listOf(
        AnalyticsParam(Param.CONVERT_TYPE, hiraKanaType),
        AnalyticsParam(Param.INPUT_TEXT_LENGTH, inputTextLength.toString()),
    )
}

class ConvertError(error: String) : AnalyticsEvent() {
    override val event = Event.CONVERT_ERROR
    override val params = listOf(
        AnalyticsParam(Param.CONVERT_ERROR, error),
    )
}

class ChangeHiraKanaType(hiraKanaType: String) : AnalyticsEvent() {
    override val event = Event.CHANGE_HIRA_KANA_TYPE
    override val params = listOf(
        AnalyticsParam(Param.CONVERT_TYPE, hiraKanaType),
    )
}

class ClearAllText : AnalyticsEvent() {
    override val event = Event.CLEAR_ALL_TEXT
}

class UpdateTheme(theme: String) : AnalyticsEvent() {
    override val event = Event.UPDATE_THEME
    override val params = listOf(
        AnalyticsParam(Param.THEME, theme),
    )
}

class UpdateLanguage(language: String) : AnalyticsEvent() {
    override val event = Event.UPDATE_LANGUAGE
    override val params = listOf(
        AnalyticsParam(Param.LANGUAGE, language),
    )
}

class UpdateFont(font: String) : AnalyticsEvent() {
    override val event = Event.UPDATE_FONT
    override val params = listOf(
        AnalyticsParam(Param.FONT, font),
    )
}

class SwitchEnableInAppUpdate(isEnable: Boolean) : AnalyticsEvent() {
    override val event = Event.SWITCH_ENABLE_IN_APP_UPDATE
    override val params = listOf(
        AnalyticsParam(Param.IS_ENABLE_IN_APP_UPDATE, isEnable.toString()),
    )
}

class TotalConvertCount(count: Int) : AnalyticsEvent() {
    override val event = Event.COUNT_UP_TOTAL_CONVERT_COUNT
    override val params = listOf(
        AnalyticsParam(Param.COUNT, count.toString()),
    )
}

class RequestReview() : AnalyticsEvent() {
    override val event = Event.REQUEST_REVIEW
}

enum class Screen {
    CONVERTER,
    HISTORY,
    SETTING,
    INFO,
    PRIVACY_POLICY,
}

enum class Event {
    CONVERT,
    CONVERT_ERROR,
    CHANGE_HIRA_KANA_TYPE,
    CLEAR_ALL_TEXT,
    UPDATE_THEME,
    UPDATE_LANGUAGE,
    UPDATE_FONT,
    SWITCH_ENABLE_IN_APP_UPDATE,
    COUNT_UP_TOTAL_CONVERT_COUNT,
    REQUEST_REVIEW,
}

enum class Param {
    CONVERT_TYPE,
    CONVERT_ERROR,
    INPUT_TEXT_LENGTH,
    THEME,
    LANGUAGE,
    FONT,
    IS_ENABLE_IN_APP_UPDATE,
    COUNT,
}
