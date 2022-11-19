package ksnd.open.hiraganaconverter.view

/**
 * テーマ切り替え用のthemeNum定義
 */
enum class ThemeNum(val num: Int) {
    NIGHT(num = 0),
    DAY(num = 1),
    AUTO(num = 2)
}

/**
 * アプリ内で選択できるフォントの定義
 */
enum class CustomFont {
    DEFAULT,
    CORPORATE_LOGO_ROUNDED,
    CORPORATE_YAWAMIN,
    NOSUTARU_DOT_M_PLUS,
    BIZ_UDGOTHIC
}
