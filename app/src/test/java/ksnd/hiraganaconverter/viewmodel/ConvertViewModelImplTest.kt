package ksnd.hiraganaconverter.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.MainDispatcherRule
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.model.usecase.ConversionFailedException
import ksnd.hiraganaconverter.model.usecase.ConvertTextUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertViewModelImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertTextUseCase = mockk<ConvertTextUseCase>(relaxUnitFun = true)
    private val viewModel = ConvertViewModelImpl(
        convertTextUseCase = convertTextUseCase,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun updateInputText_newInputText_isUpdated() {
        assertThat(viewModel.uiState.value.inputText).isEqualTo("")
        viewModel.updateInputText("漢字")
        assertThat(viewModel.uiState.value.inputText).isEqualTo("漢字")
    }

    @Test
    fun updateOutputText_newOutputText_isUpdated() {
        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
        viewModel.updateOutputText("かんじ")
        assertThat(viewModel.uiState.value.outputText).isEqualTo("かんじ")
    }

    @Test
    fun convert_notChangedText_notCalledUseCase() = runTest {
        assertThat(viewModel.uiState.value.inputText).isEqualTo("")
        viewModel.convert(timeZone = TIME_ZONE)
        coVerify(exactly = 0) { convertTextUseCase(any(), any(), any()) }
    }

    @Test
    fun convert_firstInputText_updateTexts() = runTest {
        val inputText = "漢字"
        val outputText = "かんじ"
        coEvery { convertTextUseCase(any(), any(), any()) } returns outputText
        viewModel.updateInputText(inputText)
        viewModel.convert(timeZone = TIME_ZONE)
        assertThat(viewModel.uiState.value.outputText).isEqualTo(outputText)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo(inputText)
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        coVerify(exactly = 1) { convertTextUseCase(any(), any(), any()) }
    }

    @Test
    fun convert_throwException_updateErrorType() = runTest {
        coEvery { convertTextUseCase(any(), any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        viewModel.convert(timeZone = TIME_ZONE)
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        coVerify(exactly = 1) { convertTextUseCase(any(), any(), any()) }
    }

    @Test
    fun changeHiraKanaType_differentType_isUpdatedAndClearPrevieous() = runTest {
        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
        viewModel.updateInputText("漢字")
        coEvery { convertTextUseCase(any(), any(), HiraKanaType.HIRAGANA) } returns "かんじ"
        viewModel.convert(timeZone = TIME_ZONE)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo("漢字")
        viewModel.changeHiraKanaType(HiraKanaType.KATAKANA)
        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.KATAKANA)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo("")
    }

    @Test
    fun clearConvertErrorType_once_isEmpty() = runTest {
        coEvery { convertTextUseCase(any(), any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        viewModel.convert(timeZone = TIME_ZONE)
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        viewModel.clearConvertErrorType()
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
    }

    @Test
    fun clearAllText_once_isAllEmpty() = runTest {
        coEvery { convertTextUseCase(any(), any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        viewModel.updateOutputText("カンジ")
        viewModel.convert(timeZone = TIME_ZONE)
        assertThat(viewModel.uiState.value.inputText).isNotEmpty()
        assertThat(viewModel.uiState.value.outputText).isNotEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        viewModel.clearAllText()
        assertThat(viewModel.uiState.value.inputText).isEmpty()
        assertThat(viewModel.uiState.value.outputText).isEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
    }

//    @Test
//    fun convert_sameTextTwice_doNotConvert() = runTest {
//        val inputAndPreviousText = "漢字"
//        coEvery { convertTextUseCase(any(), any(), any()) } returns inputAndPreviousText
//        viewModel.updateInputText(inputAndPreviousText)
//        viewModel.convert(timeZone = TIME_ZONE)
//        coEvery { convertTextUseCase(any(), any(), any()) } returns inputAndPreviousText
//        viewModel.updateOutputText("い")
//        assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
//        viewModel.convert(context = context)
//        assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
//    }

//    @Test
//    fun convertViewModel_convert_receiveResponse() = runTest {
//        // 変換に成功し変換後文字列が設定されていることとエラーテキストが設定されていないことを確認
//        viewModelNotReturnErrorResponse.let { viewModel ->
//            viewModel.updateInputText("文字列")
//            viewModel.convert(context = context)
//            advanceUntilIdle()
//            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
//            assertThat(viewModel.uiState.value.errorText).isEqualTo("")
//        }
//    }
//
//    @Test
//    fun convertViewModel_convert_isReachedConvertCount() = runTest {
//        // １日の変換件数の上限を超えた場合に変換を行わずエラーテキストを設定することを確認
//        val viewModel = createTestingConvertViewModel(
//            context = context,
//            testDispatcher = testDispatcher,
//            isErrorResponse = false,
//            isReachedConvertMaxLimit = true,
//        )
//        viewModel.updateInputText("文字列")
//        viewModel.convert(context = context)
//        advanceUntilIdle()
//        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
//        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
//    }
//
//    @Test
//    fun convertViewModel_convert_receiveErrorResponse() = runTest {
//        // 変換時にエラーが発生したときにエラーテキストを設定することを確認
//        val viewModel = createTestingConvertViewModel(
//            context = context,
//            testDispatcher = testDispatcher,
//            isErrorResponse = true,
//            isReachedConvertMaxLimit = false,
//        )
//        viewModel.updateInputText("文字列")
//        viewModel.convert(context = context)
//        advanceUntilIdle()
//        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
//        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
//    }
//
//    // ● updateInputText ------------------------------------------------------------------------ ●
//    @Test
//    fun convertViewModel_updateInputText_isUpdated() {
//        viewModelNotReturnErrorResponse.let { viewModel ->
//            assertThat(viewModel.uiState.value.inputText).isEqualTo("")
//            viewModel.updateInputText("あ")
//            assertThat(viewModel.uiState.value.inputText).isEqualTo("あ")
//        }
//    }
//
//    // ● updateOutputText------------------------------------------------------------------------ ●
//    @Test
//    fun convertViewModel_updateOutputText_isUpdated() {
//        viewModelNotReturnErrorResponse.let { viewModel ->
//            assertThat(viewModel.uiState.value.outputText).isEqualTo("")
//            viewModel.updateOutputText("い")
//            assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
//        }
//    }
//
//    // ● clearErrorText ------------------------------------------------------------------------- ●
//    @Test
//    fun convertViewModel_clearErrorText_isCleared() = runTest {
//        // APIでの変換に失敗したときに設定されるエラーテキストを削除できることを確認
//        val viewModel = createTestingConvertViewModel(
//            context = context,
//            testDispatcher = testDispatcher,
//            isErrorResponse = true,
//            isReachedConvertMaxLimit = false,
//        )
//        assertThat(viewModel.uiState.value.errorText).isEqualTo("")
//        viewModel.updateInputText("う")
//        viewModel.convert(context = context)
//        advanceUntilIdle()
//        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
//        viewModel.clearConvertErrorType()
//        assertThat(viewModel.uiState.value.errorText).isEqualTo("")
//    }
//
//    // ● changeHiraKanaType --------------------------------------------------------------------- ●
//    @Test
//    fun convertViewModel_updateType_isChangedAndClearPreviousInputText() = runTest {
//        // ひらがなカタカナを変更するとpreviousInputTextを初期化させるため、
//        // 変換後にinputTextを変更しなくても変換できることを確認
//        viewModelNotReturnErrorResponse.let { viewModel ->
//            viewModel.updateInputText("お")
//            viewModel.convert(context = context)
//            advanceUntilIdle()
//            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
//            assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
//            viewModel.changeHiraKanaType(type = HiraKanaType.KATAKANA)
//            assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.KATAKANA)
//            viewModel.updateOutputText("")
//            assertThat(viewModel.uiState.value.outputText).isEqualTo("")
//            viewModel.convert(context = context)
//            advanceUntilIdle()
//            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
//        }
//    }
    companion object {
        const val TIME_ZONE = "Asia/Tokyo"
    }
}

// private fun createTestingConvertViewModel(
//    context: Context,
//    testDispatcher: CoroutineDispatcher,
//    isErrorResponse: Boolean,
//    isReachedConvertMaxLimit: Boolean,
// ): ConvertViewModelImpl {
//    return ConvertViewModelImpl(
//        convertRepository = FakeConverterRepository(context, isErrorResponse),
//        dataStoreRepository = FakeDataStoreRepositoryImpl(isReachedConvertMaxLimit),
//        convertHistoryRepository = FakeConvertHistoryRepositoryImpl(),
//        ioDispatcher = testDispatcher,
//        defaultDispatcher = testDispatcher,
//    )
// }
//
// @OptIn(ExperimentalSerializationApi::class)
// private class FakeConverterRepository(
//    context: Context,
//    isErrorResponse: Boolean,
// ) : ConvertRepository {
//    private val contentType = "application/json".toMediaType()
//    private val fakeInterceptor = FakeInterceptor(
//        isErrorResponse = isErrorResponse,
//        context = context,
//    )
//    private val client = OkHttpClient.Builder().addInterceptor(fakeInterceptor).build()
//    private val convertService = Retrofit.Builder()
//        .baseUrl("https://testing")
//        .addConverterFactory(Json.asConverterFactory(contentType))
//        .client(client)
//        .build()
//        .create(ConvertApiClient::class.java)
//
//    override suspend fun requestConvert(
//        sentence: String,
//        type: String,
//        appId: String,
//    ): retrofit2.Response<ResponseData> {
//        val requestData = RequestData(
//            appId = appId,
//            sentence = sentence,
//            outputType = type,
//        )
//        val json = Json.encodeToString(requestData)
//        val body = json.toRequestBody(contentType)
//        return runBlocking { convertService.requestConvert(body) }
//    }
// }
//
// private class FakeInterceptor(
//    private val isErrorResponse: Boolean,
//    private val context: Context,
// ) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        return Response
//            .Builder()
//            .code(code = if (isErrorResponse) 400 else 200)
//            .protocol(Protocol.HTTP_2)
//            .body(
//                Json.encodeToString(
//                    ResponseData(
//                        requestId = "",
//                        outputType = "Hiragana",
//                        converted = "えー",
//                    ),
//                ).toResponseBody("application/json".toMediaType()),
//            )
//            .message(
//                message = if (isErrorResponse) {
//                    context.getString(R.string.network_error)
//                } else {
//                    "OK"
//                },
//            )
//            .request(request)
//            .build()
//    }
// }
//
// private class FakeConvertHistoryRepositoryImpl : ConvertHistoryRepository {
//    override fun insertConvertHistory(beforeText: String, afterText: String, time: String) {}
//    override fun getAllConvertHistory(): List<ConvertHistoryData> = emptyList()
//    override fun deleteAllConvertHistory() {}
//    override fun deleteConvertHistory(id: Long) {}
// }
//
// private class FakeDataStoreRepositoryImpl(
//    private val isReachedConvertMaxLimit: Boolean,
// ) : DataStoreRepository {
//    override fun selectedThemeNum(): Flow<Int> = flow { }
//    override fun selectedCustomFont(): Flow<String> = flow { }
//    override suspend fun updateThemeNum(newThemeNum: Int) {}
//    override suspend fun updateCustomFont(newCustomFont: CustomFont) {}
//    override suspend fun checkReachedConvertMaxLimit(today: String) = isReachedConvertMaxLimit
// }
