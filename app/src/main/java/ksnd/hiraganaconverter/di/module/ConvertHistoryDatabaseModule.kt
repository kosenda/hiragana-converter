package ksnd.hiraganaconverter.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.ConvertHistoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConvertHistoryDatabaseModule {
    @Provides
    @Singleton
    fun provideConvertHistoryDatabase(
        @ApplicationContext context: Context,
    ): ConvertHistoryDatabase {
        return ConvertHistoryDatabase.getInstance(context)
    }
}
