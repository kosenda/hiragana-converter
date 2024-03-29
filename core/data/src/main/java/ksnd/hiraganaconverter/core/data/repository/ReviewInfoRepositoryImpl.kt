package ksnd.hiraganaconverter.core.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsHelper
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.model.ReviewInfo
import javax.inject.Inject

class ReviewInfoRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<ReviewInfo>,
    private val analytics: AnalyticsHelper,
) : ReviewInfoRepository {
    override val reviewInfo: Flow<ReviewInfo> = dataStore.data

    override suspend fun countUpTotalConvertCount(): Int {
        val newCount = (reviewInfo.firstOrNull()?.totalConvertCount ?: 0) + 1
        dataStore.updateData { it.copy(totalConvertCount = newCount) }
        analytics.logEvent(Analytics.TotalConvertCount(count = newCount))
        return newCount
    }

    override suspend fun updateLastRequestReviewLocalDate() {
        dataStore.updateData { it.copy(lastRequestReviewLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())) }
    }

    override suspend fun completedRequestReview() {
        dataStore.updateData { it.copy(isAlreadyReviewed = true) }
    }
}
