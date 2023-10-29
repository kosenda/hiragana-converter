package ksnd.hiraganaconverter.core.analytics.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsImpl
import javax.inject.Singleton

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
