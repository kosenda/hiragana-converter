package ksnd.open.hiraganaconverter.di.module

import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.SharedPreferencesRepository
import ksnd.open.hiraganaconverter.model.repository.SharedPreferencesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesRepositoryModule {
    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(
        sharedPreferences: SharedPreferences
    ): SharedPreferencesRepositoryImpl {
        return SharedPreferencesRepositoryImpl(sharedPreferences = sharedPreferences)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPreferencesRepositoryBindModule {

    @Binds
    abstract fun bindSharedPreferencesRepository(
        impl: SharedPreferencesRepositoryImpl
    ): SharedPreferencesRepository
}
