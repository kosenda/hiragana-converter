package ksnd.open.hiraganaconverter.di.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.ThemeRepository
import ksnd.open.hiraganaconverter.model.repository.ThemeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeRepositoryModule {
    @Provides
    @Singleton
    fun provideThemeRepository(preferencesDataStore: DataStore<Preferences>): ThemeRepositoryImpl {
        return ThemeRepositoryImpl(preferencesDataStore = preferencesDataStore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeRepositoryBindModule {

    @Binds
    abstract fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository
}
