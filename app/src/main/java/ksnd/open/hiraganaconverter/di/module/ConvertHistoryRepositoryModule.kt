package ksnd.open.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertHistoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertHistoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindConvertHistoryRepository(
        impl: ConvertHistoryRepositoryImpl
    ): ConvertHistoryRepository
}
