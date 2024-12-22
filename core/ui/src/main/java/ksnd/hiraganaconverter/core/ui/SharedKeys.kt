package ksnd.hiraganaconverter.core.ui

data class ConvertHistorySharedKey(
    val id: Long,
    val type: SharedType,
    val origin: String = "convertHistory",
)

enum class SharedType {
    CONVERT_TIME,
    BEFORE_TEXT,
}
