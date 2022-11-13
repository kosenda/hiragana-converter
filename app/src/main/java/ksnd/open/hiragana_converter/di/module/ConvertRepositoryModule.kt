package ksnd.open.hiragana_converter.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiragana_converter.model.repository.ConvertRepository
import ksnd.open.hiragana_converter.model.repository.ConvertRepositoryImpl
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