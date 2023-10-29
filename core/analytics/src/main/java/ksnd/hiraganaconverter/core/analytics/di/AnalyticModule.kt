package ksnd.hiraganaconverter.core.analytics.di

import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.analytics.Analytics
import ksnd.hiraganaconverter.core.analytics.AnalyticsImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticModule {
    @Provides
    @Singleton
    fun provideAnalytics(): Analytics = AnalyticsImpl(firebaseAnalytics = Firebase.analytics)
}
