package ksnd.hiraganaconverter.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.domain.interceptor.ErrorInterceptor
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorInterceptorModule {
    @Binds
    @Singleton
    abstract fun bindErrorInterceptor(impl: ErrorInterceptor): Interceptor
}
