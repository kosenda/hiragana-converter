package ksnd.hiraganaconverter.core.model.ui

enum class ConvertErrorType {
    TOO_MANY_CHARACTER,
    RATE_LIMIT_EXCEEDED,
    CONVERSION_FAILED,
    INTERNAL_SERVER,
    NETWORK,
    REACHED_CONVERT_MAX_LIMIT,
}
