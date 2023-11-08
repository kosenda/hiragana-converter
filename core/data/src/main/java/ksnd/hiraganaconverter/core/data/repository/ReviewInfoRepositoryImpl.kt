package ksnd.hiraganaconverter.core.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.model.ReviewInfo
import javax.inject.Inject

class ReviewInfoRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<ReviewInfo>,
) : ReviewInfoRepository {
    override val reviewInfo: Flow<ReviewInfo> = dataStore.data

    override suspend fun countUpTotalConvertCount(): Int {
        val newCount = (reviewInfo.firstOrNull()?.totalConvertCount ?: 0) + 1
        dataStore.updateData { it.copy(totalConvertCount = newCount) }
        return newCount
    }
}
