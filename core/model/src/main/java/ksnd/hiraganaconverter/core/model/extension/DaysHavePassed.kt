package ksnd.hiraganaconverter.core.model.extension

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.until

fun LocalDate.daysHavePassed(today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): Int {
    return 0.minus(today.until(this, DateTimeUnit.DAY))
}
