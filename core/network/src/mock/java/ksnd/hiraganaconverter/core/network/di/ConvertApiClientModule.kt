package ksnd.hiraganaconverter.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.network.impl.MockConvertApiClient
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertApiClientModule {
    @Binds
    @Singleton
    abstract fun bindMockConvertApiClient(mock: MockConvertApiClient): ConvertApiClient
}
