package ksnd.hiraganaconverter.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.data.database.ConvertHistoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConvertHistoryDatabaseModule {
    @Provides
    @Singleton
    fun provideConvertHistoryDatabase(
        @ApplicationContext context: Context,
    ): ConvertHistoryDatabase = ConvertHistoryDatabase.getInstance(context)
}
