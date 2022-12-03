package ksnd.open.hiraganaconverter.model

import android.text.format.DateFormat
import java.util.Calendar
import java.util.TimeZone

fun getNowTime(timeZone: String, format: String): String {
    return DateFormat.format(
        format,
        Calendar.getInstance(
            if ("default" == timeZone) {
                TimeZone.getDefault()
            } else {
                TimeZone.getTimeZone(timeZone)
            }
        )
    ).toString()
}
