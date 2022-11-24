package ksnd.open.hiraganaconverter.di.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.open.hiraganaconverter.model.repository.DataStoreRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreRepositoryModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(preferencesDataStore: DataStore<Preferences>): DataStoreRepositoryImpl {
        return DataStoreRepositoryImpl(preferencesDataStore = preferencesDataStore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreRepositoryBindModule {

    @Binds
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository
}
