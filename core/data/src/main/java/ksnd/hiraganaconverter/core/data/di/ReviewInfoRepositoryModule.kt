package ksnd.hiraganaconverter.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.core.data.repository.ReviewInfoRepositoryImpl
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewInfoRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindConvertHistoryRepository(impl: ReviewInfoRepositoryImpl): ReviewInfoRepository
}
