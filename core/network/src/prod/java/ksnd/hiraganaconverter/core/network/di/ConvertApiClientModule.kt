package ksnd.hiraganaconverter.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import ksnd.hiraganaconverter.core.network.impl.ConvertApiClientImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertApiClientModule {
    @Binds
    @Singleton
    abstract fun bindConvertApiClient(impl: ConvertApiClientImpl): ConvertApiClient
}
