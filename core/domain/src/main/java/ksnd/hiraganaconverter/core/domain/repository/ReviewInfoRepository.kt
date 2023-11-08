package ksnd.hiraganaconverter.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.model.ReviewInfo

interface ReviewInfoRepository {
    val reviewInfo: Flow<ReviewInfo>
    suspend fun countUpTotalConvertCount(): Int
}
