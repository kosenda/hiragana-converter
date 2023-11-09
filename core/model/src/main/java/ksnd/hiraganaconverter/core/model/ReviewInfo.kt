package ksnd.hiraganaconverter.core.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ReviewInfo(
    val isAlreadyReviewed: Boolean = false,
    val totalConvertCount: Int = 0,
    val lastRequestReviewLocalDate: LocalDate? = null,
)
