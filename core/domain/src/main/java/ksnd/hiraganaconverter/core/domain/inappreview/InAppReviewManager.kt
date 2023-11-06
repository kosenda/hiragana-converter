package ksnd.hiraganaconverter.core.domain.inappreview

interface InAppReviewManager {
    suspend fun requestReview()
}
