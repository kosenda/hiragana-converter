package ksnd.hiraganaconverter.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.data.di.ReviewInfoSerializer
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReviewInfoRepositoryImplTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dataStore = DataStoreFactory.create(
        serializer = ReviewInfoSerializer,
        produceFile = { context.preferencesDataStoreFile("TestReviewInfoDataStore") },
    )
    private val repository = ReviewInfoRepositoryImpl(dataStore = dataStore)

    @Test

    fun countUpTotalConvertCount_first_is1() = runTest {
        assertThat(repository.countUpTotalConvertCount()).isEqualTo(1)
    }

    @Test
    fun countUpTotalConvertCount_countUp_isCountUp() = runTest {
        repeat(5) { assertThat(repository.countUpTotalConvertCount()).isEqualTo(it + 1) }
    }
}
