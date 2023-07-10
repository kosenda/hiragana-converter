package ksnd.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.repository.SharedPreferencesRepository
import ksnd.hiraganaconverter.model.repository.SharedPreferencesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSharedPreferencesRepository(
        impl: SharedPreferencesRepositoryImpl,
    ): SharedPreferencesRepository
}
