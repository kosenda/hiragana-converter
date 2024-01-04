package ksnd.hiraganaconverter.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.data.repository.ConvertRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindConvertRepository(impl: ConvertRepositoryImpl): ConvertRepository
}
