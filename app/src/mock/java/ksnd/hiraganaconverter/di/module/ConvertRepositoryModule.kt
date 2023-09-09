package ksnd.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.repository.ConvertRepository
import ksnd.hiraganaconverter.model.repository.MockConvertRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindConvertRepository(impl: MockConvertRepository): ConvertRepository
}
