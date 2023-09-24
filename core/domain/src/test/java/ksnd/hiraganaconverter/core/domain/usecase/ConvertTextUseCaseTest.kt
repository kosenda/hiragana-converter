package ksnd.hiraganaconverter.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
class ConvertTextUseCaseTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertRepository = mockk<ConvertRepository>(relaxed = true)
    private val dataStoreRepository = mockk<DataStoreRepository>(relaxUnitFun = true)
    private val convertHistoryRepository = mockk<ConvertHistoryRepository>(relaxUnitFun = true)
    private val useCase = ConvertTextUseCase(
        convertRepository = convertRepository,
        dataStoreRepository = dataStoreRepository,
        convertHistoryRepository = convertHistoryRepository,
        appConfig = mockk(relaxed = true),
    )

    @Test
    fun invoke_overConvertForReachedConvert_isReachedConvertMaxLimitException() = runTest {
        coEvery { dataStoreRepository.checkIsExceedingMaxLimit() } returns true
        assertFailsWith<IsReachedConvertMaxLimitException> {
            useCase(inputText = INPUT_TXT, selectedTextType = SELECTED_TYPE)
        }
        coVerify(exactly = 1) { dataStoreRepository.checkIsExceedingMaxLimit() }
    }

    @Test
    fun invoke_responseIsNothing_conversionFailedException() = runTest {
        coEvery { dataStoreRepository.checkIsExceedingMaxLimit() } returns false
        coEvery { convertRepository.requestConvert(any(), any(), any()) } returns null
        assertFailsWith<ConversionFailedException> {
            useCase(inputText = INPUT_TXT, selectedTextType = SELECTED_TYPE)
        }
        coVerify(exactly = 1) { convertRepository.requestConvert(any(), any(), any()) }
    }

    @Test
    fun invoke_error413_conversionFailedException() = runTest {
        coEvery { dataStoreRepository.checkIsExceedingMaxLimit() } returns false
        coEvery { convertRepository.requestConvert(any(), any(), any()) } returns errorResponse
        assertFailsWith<InterceptorError> {
            useCase(inputText = INPUT_TXT, selectedTextType = SELECTED_TYPE)
        }
        coVerify(exactly = 1) { convertRepository.requestConvert(any(), any(), any()) }
    }

    @Test
    fun invoke_relaxed_outputConverted() = runTest {
        coEvery { dataStoreRepository.checkIsExceedingMaxLimit() } returns false
        coEvery { convertRepository.requestConvert(any(), any(), any()) } returns successResponse
        assertThat(useCase(inputText = INPUT_TXT, selectedTextType = SELECTED_TYPE)).isNotEmpty()
        coVerify(exactly = 1) { convertRepository.requestConvert(any(), any(), any()) }
    }

    companion object {
        private const val INPUT_TXT = "漢字"
        private val SELECTED_TYPE = HiraKanaType.HIRAGANA
        private const val error413Json = """{"error": {"code": 413, "message": "TOO_MANY_CHARACTER"}"""
        private val errorResponse: Response<ResponseData> = Response.error(413, error413Json.toResponseBody("application/json".toMediaType()))
        private val successResponse: Response<ResponseData> = Response.success(
            ResponseData(converted = "かんじ", outputType = "hiragana", requestId = "labs.goo.ne.jp\temp\t0"),
        )
    }
}
