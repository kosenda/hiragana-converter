package ksnd.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepositoryImpl
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
