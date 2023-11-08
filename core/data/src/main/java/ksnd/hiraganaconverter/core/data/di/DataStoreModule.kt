package ksnd.hiraganaconverter.core.data.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ksnd.hiraganaconverter.core.model.ReviewInfo
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("DataStore")
            },
        )
    }

    @Provides
    @Singleton
    fun provideCalcInputRequiredInterestDataStore(
        @ApplicationContext context: Context,
    ): DataStore<ReviewInfo> {
        return DataStoreFactory.create(
            serializer = ReviewInfoSerializer,
            produceFile = { context.preferencesDataStoreFile("ReviewInfoDataStore") },
        )
    }
}

object ReviewInfoSerializer : Serializer<ReviewInfo> {
    override val defaultValue = ReviewInfo()
    override suspend fun readFrom(input: InputStream): ReviewInfo {
        try {
            return Json.decodeFromString(ReviewInfo.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read data", serialization)
        }
    }
    override suspend fun writeTo(t: ReviewInfo, output: OutputStream) {
        output.write(Json.encodeToString(ReviewInfo.serializer(), t).encodeToByteArray())
    }
}
