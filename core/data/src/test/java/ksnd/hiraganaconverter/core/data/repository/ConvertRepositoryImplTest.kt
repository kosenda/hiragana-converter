package ksnd.hiraganaconverter.core.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class ConvertRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertApiClient = mockk<ConvertApiClient>()
    private val repository = ConvertRepositoryImpl(
        convertApiClient = convertApiClient,
    )

    @Test
    fun requestConvert_notError_returnResponse() = runTest {
        coEvery {
            convertApiClient.requestConvert(
                appId = APP_ID,
                sentence = SENTENCE,
                type = TYPE,
            )
        } returns retrofit2.Response.success(RESPONSE_DATA)

        val response = repository.requestConvert(sentence = SENTENCE, type = TYPE, appId = APP_ID)

        assertThat(response).isInstanceOf(retrofit2.Response::class.java)
        assertThat(response?.isSuccessful).isTrue()
        coVerify(exactly = 1) { convertApiClient.requestConvert(appId = APP_ID, sentence = SENTENCE, type = TYPE) }
    }

    @Test
    fun requestConvert_error_returnNull() = runTest {
        coEvery {
            convertApiClient.requestConvert(
                appId = APP_ID,
                sentence = SENTENCE,
                type = TYPE,
            )
        } throws Exception()

        val response = repository.requestConvert(sentence = SENTENCE, type = TYPE, appId = APP_ID)

        assertThat(response).isNull()
        coVerify(exactly = 1) { convertApiClient.requestConvert(appId = APP_ID, sentence = SENTENCE, type = TYPE) }
    }

    companion object {
        private const val SENTENCE = "sentence"
        private const val TYPE = "type"
        private const val APP_ID = "appId"
        private val RESPONSE_DATA = ResponseData("かんじ", "hiragana", "test")
    }
}
