package ksnd.hiraganaconverter.core.analytics.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsImpl
import javax.inject.Singleton

/**
 * ref: https://github.com/android/nowinandroid/blob/15f8861da15ff75876af0f1383d23417eb79d89e/core/analytics/src/prod/kotlin/com/google/samples/apps/nowinandroid/core/analytics/AnalyticsModule.kt
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticModule {
    @Binds
    @Singleton
    abstract fun provideAnalytics(impl: AnalyticsImpl): Analytics

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
    }
}
