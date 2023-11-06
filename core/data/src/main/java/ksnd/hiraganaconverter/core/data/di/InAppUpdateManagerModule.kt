package ksnd.hiraganaconverter.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateManagerImpl
import ksnd.hiraganaconverter.core.domain.inappupdate.InAppUpdateManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InAppUpdateManagerModule {
    @Binds
    @Singleton
    abstract fun provideInAppUpdateManager(impl: InAppUpdateManagerImpl): InAppUpdateManager
}
