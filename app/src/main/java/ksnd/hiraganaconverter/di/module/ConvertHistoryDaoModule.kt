package ksnd.hiraganaconverter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.model.ConvertHistoryDao
import ksnd.hiraganaconverter.model.ConvertHistoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConvertHistoryDaoModule {
    @Provides
    @Singleton
    fun provideConvertHistoryDao(database: ConvertHistoryDatabase): ConvertHistoryDao {
        return database.convertHistoryDao()
    }
}
