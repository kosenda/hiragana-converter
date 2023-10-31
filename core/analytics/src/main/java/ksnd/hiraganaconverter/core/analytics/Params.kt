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
}

enum class Param {
    CONVERT_TYPE,
}

enum class ConvertType {
    HIRAGANA,
    KATAKANA,
}
