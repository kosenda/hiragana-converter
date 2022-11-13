package ksnd.hiraganaconverter.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ksnd.open.hiragana_converter.R
import ksnd.open.hiragana_converter.model.CustomFont

/**
 * Data Preferencesで管理しているフォントの種類をテーマに反映させるための処理
 */
fun typography(customFont: String): Typography {

    val selectedFont = when(customFont) {

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

    return Typography(
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp
        ),
        titleLarge = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp
        ),
        titleMedium = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),
        bodySmall = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        labelLarge = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = selectedFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )
}