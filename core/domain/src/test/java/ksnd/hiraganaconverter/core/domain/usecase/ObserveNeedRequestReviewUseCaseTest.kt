package ksnd.hiraganaconverter.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.model.ReviewInfo
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class ObserveNeedRequestReviewUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val reviewInfoRepository = mockk<ReviewInfoRepository>(relaxUnitFun = true)
    private val useCase = ObserveNeedRequestReviewUseCase(
        reviewInfoRepository = reviewInfoRepository,
    )

    @Test
    fun invoke_alreadyReview_isFalse() = runTest {
        every { reviewInfoRepository.reviewInfo } returns flowOf(ReviewInfo(isAlreadyReviewed = true))
        assertThat(useCase().first()).isFalse()
    }

    @Test
    fun invoke_firstConvert_isFalse() = runTest {
        every { reviewInfoRepository.reviewInfo } returns flowOf(ReviewInfo(isAlreadyReviewed = false, totalConvertCount = 1))
        assertThat(useCase().first()).isFalse()
    }

    @Test
    fun invoke_convertIntervalDateTimesAndNotPassedIntervalCount_isFalse() = runTest {
        every { reviewInfoRepository.reviewInfo } returns flowOf(
            ReviewInfo(
                isAlreadyReviewed = false,
                totalConvertCount = INTERVAL_REQUEST_REVIEW_COUNT,
                lastRequestReviewLocalDate = YesterdayLocalDate,
            )
        )
        assertThat(useCase().first()).isFalse()
    }

    @Test
    fun invoke_convertIntervalDateTimesAndPassedIntervalCount_isFalse() = runTest {
        every { reviewInfoRepository.reviewInfo } returns flowOf(
            ReviewInfo(
                isAlreadyReviewed = false,
                totalConvertCount = INTERVAL_REQUEST_REVIEW_COUNT,
                lastRequestReviewLocalDate = IntervalDateDaysAgoLocalDate,
            )
        )
        assertThat(useCase().first()).isTrue()
    }

    companion object {
        val YesterdayLocalDate = Clock.System
            .todayIn(TimeZone.currentSystemDefault()).minus(1, DateTimeUnit.DAY)
        val IntervalDateDaysAgoLocalDate = Clock.System
            .todayIn(TimeZone.currentSystemDefault()).minus(INTERVAL_REQUEST_REVIEW_DATE + 1, DateTimeUnit.DAY)
    }
}