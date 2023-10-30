package ksnd.hiraganaconverter.core.analytics

enum class Screen {
    CONVERTER,
    HISTORY,
    SETTING,
    INFO,
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
