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
}

enum class Param {
    CONVERT_TYPE,
    CONVERT_ERROR,
    INPUT_TEXT_LENGTH,
}
