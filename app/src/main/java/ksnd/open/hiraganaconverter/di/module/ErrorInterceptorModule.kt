package ksnd.open.hiraganaconverter.di.module

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.ErrorInterceptor
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorInterceptorModule {
    @Binds
    @Singleton
    abstract fun bindErrorInterceptor(impl: ErrorInterceptor): Interceptor
}
