package ksnd.hiraganaconverter.core.data.di

import android.content.Context
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import ksnd.hiraganaconverter.core.data.inappreview.InAppReviewManagerImpl
import ksnd.hiraganaconverter.core.domain.inappreview.InAppReviewManager

@Module
@InstallIn(ActivityComponent::class)
abstract class InAppReviewManagerModule {
    @Binds
    abstract fun provideInAppReviewManager(impl: InAppReviewManagerImpl): InAppReviewManager

    companion object {
        @Provides
        fun provideAppReviewManager(@ActivityContext context: Context): ReviewManager {
            return ReviewManagerFactory.create(context)
        }
    }
}
