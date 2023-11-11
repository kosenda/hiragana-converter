package ksnd.hiraganaconverter.core.model.extension

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class DaysHavePassedKtTest(
    private val testDate: TestDate
) {
    @Test
    fun testDaysHavePassed() {
        assertThat(testDate.inputDate.daysHavePassed(today = TODAY)).isEqualTo(testDate.expected)
    }

    companion object {
        @Parameters
        @JvmStatic
        fun localDate(): Iterable<TestDate> {
            return listOf(
                TestDate(inputDate = LocalDate(year = 2023, monthNumber = 11, dayOfMonth = 11), expected = 0),
                TestDate(inputDate = LocalDate(year = 2023, monthNumber = 11, dayOfMonth = 10), expected = 1),
                TestDate(inputDate = LocalDate(year = 2023, monthNumber = 11, dayOfMonth = 12), expected = -1),
            )
        }
        data class TestDate(val inputDate: LocalDate, val expected: Int)
        val TODAY = LocalDate(year = 2023, monthNumber = 11, dayOfMonth = 11)
    }
}
