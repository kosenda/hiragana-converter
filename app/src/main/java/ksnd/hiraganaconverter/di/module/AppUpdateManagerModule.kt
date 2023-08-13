package ksnd.hiraganaconverter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.InAppUpdateManager
import ksnd.hiraganaconverter.model.InAppUpdateManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppUpdateManagerModule {
    @Provides
    @Singleton
    fun provideAppUpdateManager(appUpdateManagerImpl: InAppUpdateManagerImpl): InAppUpdateManager {
        return appUpdateManagerImpl
    }
}
