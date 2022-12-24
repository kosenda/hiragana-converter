package ksnd.open.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.SharedPreferencesRepository
import ksnd.open.hiraganaconverter.model.repository.SharedPreferencesRepositoryImpl
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
