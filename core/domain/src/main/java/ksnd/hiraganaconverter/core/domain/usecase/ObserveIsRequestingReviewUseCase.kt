package ksnd.hiraganaconverter.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsHelper
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.model.extension.daysHavePassed
import timber.log.Timber
import javax.inject.Inject

const val INTERVAL_REQUEST_REVIEW_COUNT = 10
const val INTERVAL_REQUEST_REVIEW_DATE = 4

class ObserveIsRequestingReviewUseCase @Inject constructor(
    private val reviewInfoRepository: ReviewInfoRepository,
    private val analytics: AnalyticsHelper,
) {
    operator fun invoke(): Flow<Boolean> = reviewInfoRepository.reviewInfo
        .map {
            if (it.isAlreadyReviewed) return@map false
            val isReachedConvertCount = it.totalConvertCount != 0 && it.totalConvertCount % INTERVAL_REQUEST_REVIEW_COUNT == 0
            val isPassedIntervalDate = (it.lastRequestReviewLocalDate?.daysHavePassed() ?: Int.MAX_VALUE) >= INTERVAL_REQUEST_REVIEW_DATE
            Timber.d(
                message = "observe review info totalConvertCount=%s, daysHavePassed=%s",
                it.totalConvertCount,
                it.lastRequestReviewLocalDate?.daysHavePassed(),
            )
            isReachedConvertCount && isPassedIntervalDate
        }
        .distinctUntilChanged()
        .onEach { isRequestingReview ->
            if (isRequestingReview) analytics.logEvent(Analytics.RequestReview())
        }
}
