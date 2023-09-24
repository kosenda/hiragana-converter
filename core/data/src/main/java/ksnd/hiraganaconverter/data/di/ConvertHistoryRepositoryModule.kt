package ksnd.hiraganaconverter.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.data.repository.ConvertHistoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertHistoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindConvertHistoryRepository(
        impl: ConvertHistoryRepositoryImpl,
    ): ConvertHistoryRepository
}
