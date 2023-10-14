package ksnd.hiraganaconverter.core.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.interceptor.ErrorInterceptor
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test

class ConvertRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val errorInterceptor = mockk<ErrorInterceptor>()
    private val repository = ConvertRepositoryImpl(
        errorInterceptor = errorInterceptor,
    )

    @Test
    fun requestConvert_success_isSuccessfulIsTrue() = runTest {
        coEvery {
            errorInterceptor.intercept(any())
        } returns Response.Builder()
            .request(Request.Builder().url("https://labs.goo.ne.jp/api/hiragana/").build())
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(successResponse)
            .build()
        val response = repository.requestConvert(SENTENCE, TYPE, APP_ID)
        assertThat(response).isInstanceOf(retrofit2.Response::class.java)
        assertThat(response?.isSuccessful).isTrue()
        coVerify(exactly = 1) { errorInterceptor.intercept(any()) }
    }

    @Test
    fun requestConvert_error_isSuccessfulIsFalse() = runTest {
        coEvery {
            errorInterceptor.intercept(any())
        } returns Response.Builder()
            .request(Request.Builder().url("https://labs.goo.ne.jp/api/hiragana/").build())
            .code(413)
            .protocol(Protocol.HTTP_2)
            .message("NG")
            .body(error413response)
            .build()
        val response = repository.requestConvert(SENTENCE, TYPE, APP_ID)
        assertThat(response).isInstanceOf(retrofit2.Response::class.java)
        assertThat(response?.isSuccessful).isFalse()
        coVerify(exactly = 1) { errorInterceptor.intercept(any()) }
    }

    companion object {
        private const val SENTENCE = "sentence"
        private const val TYPE = "type"
        private const val APP_ID = "appId"
        private val successResponse = """{"converted": "かんじ", "output_type": "hiragana", "request_id": "test"}"""
            .toResponseBody("application/json".toMediaType())
        private val error413response = """{"error": {"code": 413, "message": "TOO_MANY_CHARACTER"}"""
            .toResponseBody("application/json".toMediaType())
    }
}
