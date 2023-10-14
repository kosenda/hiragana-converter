package ksnd.hiraganaconverter.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.data.repository.ConvertHistoryRepositoryImpl
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
