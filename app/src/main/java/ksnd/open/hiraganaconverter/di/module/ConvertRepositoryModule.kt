package ksnd.open.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.ConvertRepository
import ksnd.open.hiraganaconverter.model.repository.ConvertRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConvertRepositoryModule {
    @Provides
    @Singleton
    fun provideConvertRepository(): ConvertRepositoryImpl {
        return ConvertRepositoryImpl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertRepositoryBindModule {

    @Binds
    abstract fun bindConvertRepository(impl: ConvertRepositoryImpl): ConvertRepository
}
