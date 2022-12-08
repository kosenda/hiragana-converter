package ksnd.open.hiraganaconverter.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksnd.open.hiraganaconverter.model.ErrorInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorInterceptorModule {
    @Provides
    @Singleton
    fun provideErrorInterceptor(@ApplicationContext context: Context): ErrorInterceptor {
        return ErrorInterceptor(context)
    }
}
