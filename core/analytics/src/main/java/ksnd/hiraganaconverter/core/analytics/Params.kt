package ksnd.hiraganaconverter.core.analytics

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
