package ksnd.hiraganaconverter.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.ErrorInterceptor
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorInterceptorModule {
    @Binds
    @Singleton
    abstract fun bindErrorInterceptor(impl: ErrorInterceptor): Interceptor
}
