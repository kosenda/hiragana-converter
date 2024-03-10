package ksnd.hiraganaconverter.core.data.inappreview

import android.app.Activity
import android.content.Context
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import dagger.hilt.android.qualifiers.ActivityContext
import ksnd.hiraganaconverter.core.domain.inappreview.InAppReviewManager
import javax.inject.Inject

class InAppReviewManagerImpl @Inject constructor(
    private val reviewManger: ReviewManager,
    @ActivityContext private val context: Context,
): InAppReviewManager {
    override suspend fun requestReview() {
        val reviewInfo = reviewManger.requestReview()
        reviewManger.launchReview(activity = context as Activity, reviewInfo = reviewInfo)
    }
}
