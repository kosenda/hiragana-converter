package ksnd.hiraganaconverter.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.inappupdate.InAppUpdateManager
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InAppUpdateManagerModule {
    @Provides
    @Singleton
    fun provideInAppUpdateManager(inAppUpdateManagerImpl: InAppUpdateManagerImpl): InAppUpdateManager {
        return inAppUpdateManagerImpl
    }
}
