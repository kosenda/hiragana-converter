package ksnd.open.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.ConvertRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindConvertRepository(impl: ConvertRepositoryImpl): ConvertRepository
}
