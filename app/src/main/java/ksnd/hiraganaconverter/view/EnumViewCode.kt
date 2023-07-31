package ksnd.hiraganaconverter.view

enum class Theme(val num: Int) {
    NIGHT(num = 0),
    DAY(num = 1),
    AUTO(num = 2),
}

enum class FontType(val fontName: String) {
    DEFAULT(fontName = "Default"),
    ROCKN_ROLL_ONE(fontName = "RocknRoll One"),
    HACHI_MARU_POP(fontName = "Hachi Maru Pop"),
    M_PLUS_ROUNDED_1C(fontName = "M PLUS Rounded 1c"),
    DELA_GOTHIC_ONE(fontName = "Dela Gothic One"),
    DOT_GOTHIC_16(fontName = "DotGothic16"),
    YUSEI_MAGIC(fontName = "Yusei Magic"),
    POTTA_ONE(fontName = "Potta One"),
}
