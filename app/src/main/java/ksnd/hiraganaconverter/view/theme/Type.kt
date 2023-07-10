package ksnd.hiraganaconverter.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.view.CustomFont

fun fontFamily(customFont: String): FontFamily {
    return when (customFont) {
        // https://logotype.jp/font-corpmaru.html
        CustomFont.CORPORATE_LOGO_ROUNDED.name ->
            FontFamily(Font(R.font.corporate_logo_rounded_bold_ver3))

        // https://logotype.jp/corp-yawamin.html
        CustomFont.CORPORATE_YAWAMIN.name ->
            FontFamily(Font(R.font.corporate_yawamin_ver3))

        // https://logotype.jp/nosutaru-dot.html
        CustomFont.NOSUTARU_DOT_M_PLUS.name ->
            FontFamily(Font(R.font.nosutaru_dotmplush_10_regular))

        // https://fonts.google.com/specimen/BIZ+UDGothic
        CustomFont.BIZ_UDGOTHIC.name ->
            FontFamily(Font(R.font.bizudgothic_regular))

        else -> FontFamily.Default
    }
}

fun typography(customFont: String): Typography {
    val selectedFontFamily = fontFamily(customFont = customFont)

    return Typography(
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
    )
}
