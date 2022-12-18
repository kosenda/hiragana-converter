package ksnd.open.hiraganaconverter.model

import android.text.format.DateFormat
import java.util.Calendar
import java.util.TimeZone

enum class TimeFormat(val text: String) {
    YEAR_MONTH_DATE_HOUR_MINUTE(text = "yyyy/MM/dd HH:mm"),
    YEAR_MONTH_DATE(text = "yyyy-MM-dd"),
}

fun getNowTime(timeZone: String, format: TimeFormat): String {
    return DateFormat.format(
        format.text,
        Calendar.getInstance(
            if ("default" == timeZone) {
                TimeZone.getDefault()
            } else {
                TimeZone.getTimeZone(timeZone)
            }
        )
    ).toString()
}
