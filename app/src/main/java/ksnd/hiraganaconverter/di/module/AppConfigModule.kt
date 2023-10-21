package ksnd.hiraganaconverter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksnd.hiraganaconverter.BuildConfig
import ksnd.hiraganaconverter.core.resource.AppConfig

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {
    @Provides
    fun provideAppConfig(): AppConfig =
        AppConfig(
            apiKey = BuildConfig.apiKey,
            applicationId = BuildConfig.APPLICATION_ID,
            buildType = BuildConfig.BUILD_TYPE,
            isDebug = BuildConfig.DEBUG,
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME,
        )
}
