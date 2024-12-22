package ksnd.hiraganaconverter.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.data.database.ConvertHistoryDao
import ksnd.hiraganaconverter.core.data.database.ConvertHistoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConvertHistoryDaoModule {
    @Provides
    @Singleton
    fun provideConvertHistoryDao(database: ConvertHistoryDatabase): ConvertHistoryDao = database.convertHistoryDao()
}
