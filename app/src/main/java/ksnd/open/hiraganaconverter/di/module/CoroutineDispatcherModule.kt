package ksnd.open.hiraganaconverter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {
    @IODispatcher
    @Provides
    @Singleton
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @DefaultDispatcher
    @Provides
    @Singleton
    fun provideDefaultCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}
