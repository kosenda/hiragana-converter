package ksnd.hiraganaconverter.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ksnd.hiraganaconverter.R
import ksnd.hiraganaconverter.model.ConvertApiClient
import ksnd.hiraganaconverter.model.ConvertHistoryData
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.model.RequestData
import ksnd.hiraganaconverter.model.ResponseData
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.model.repository.ConvertRepository
import ksnd.hiraganaconverter.model.repository.DataStoreRepository
import ksnd.hiraganaconverter.view.CustomFont
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ConvertViewModelImplTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = StandardTestDispatcher()
    private val viewModelNotReturnErrorResponse = createTestingConvertViewModel(
        context = context,
        testDispatcher = testDispatcher,
        isErrorResponse = false,
        isReachedConvertMaxLimit = false,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun convertViewModel_initialization_setDefault() = runTest {
        viewModelNotReturnErrorResponse.uiState.value.let { uiState ->
            assertThat(uiState.inputText).isEqualTo("")
            assertThat(uiState.outputText).isEqualTo("")
            assertThat(uiState.errorText).isEqualTo("")
            assertThat(uiState.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
        }
    }

    // ● convert -------------------------------------------------------------------------------- ●
    @Test
    fun convertViewModel_convert_NotChangeInputTextDoNotReturnResponse() = runTest {
        // 前回の変換のテキストが同じかつ変換タイプも同じ場合は変換処理を行わないことを確認
        viewModelNotReturnErrorResponse.let { viewModel ->
            viewModel.updateInputText("ああ")
            viewModel.updateOutputText("い")
            viewModel.convert(context = context)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("い")
            viewModel.updateOutputText("い")
            assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
            viewModel.convert(context = context)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
        }
    }

    @Test
    fun convertViewModel_convert_receiveResponse() = runTest {
        // 変換に成功し変換後文字列が設定されていることとエラーテキストが設定されていないことを確認
        viewModelNotReturnErrorResponse.let { viewModel ->
            viewModel.updateInputText("文字列")
            viewModel.convert(context = context)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
            assertThat(viewModel.uiState.value.errorText).isEqualTo("")
        }
    }

    @Test
    fun convertViewModel_convert_isReachedConvertCount() = runTest {
        // １日の変換件数の上限を超えた場合に変換を行わずエラーテキストを設定することを確認
        val viewModel = createTestingConvertViewModel(
            context = context,
            testDispatcher = testDispatcher,
            isErrorResponse = false,
            isReachedConvertMaxLimit = true,
        )
        viewModel.updateInputText("文字列")
        viewModel.convert(context = context)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
    }

    @Test
    fun convertViewModel_convert_receiveErrorResponse() = runTest {
        // 変換時にエラーが発生したときにエラーテキストを設定することを確認
        val viewModel = createTestingConvertViewModel(
            context = context,
            testDispatcher = testDispatcher,
            isErrorResponse = true,
            isReachedConvertMaxLimit = false,
        )
        viewModel.updateInputText("文字列")
        viewModel.convert(context = context)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
    }

    // ● updateInputText ------------------------------------------------------------------------ ●
    @Test
    fun convertViewModel_updateInputText_isUpdated() {
        viewModelNotReturnErrorResponse.let { viewModel ->
            assertThat(viewModel.uiState.value.inputText).isEqualTo("")
            viewModel.updateInputText("あ")
            assertThat(viewModel.uiState.value.inputText).isEqualTo("あ")
        }
    }

    // ● updateOutputText------------------------------------------------------------------------ ●
    @Test
    fun convertViewModel_updateOutputText_isUpdated() {
        viewModelNotReturnErrorResponse.let { viewModel ->
            assertThat(viewModel.uiState.value.outputText).isEqualTo("")
            viewModel.updateOutputText("い")
            assertThat(viewModel.uiState.value.outputText).isEqualTo("い")
        }
    }

    // ● clearErrorText ------------------------------------------------------------------------- ●
    @Test
    fun convertViewModel_clearErrorText_isCleared() = runTest {
        // APIでの変換に失敗したときに設定されるエラーテキストを削除できることを確認
        val viewModel = createTestingConvertViewModel(
            context = context,
            testDispatcher = testDispatcher,
            isErrorResponse = true,
            isReachedConvertMaxLimit = false,
        )
        assertThat(viewModel.uiState.value.errorText).isEqualTo("")
        viewModel.updateInputText("う")
        viewModel.convert(context = context)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.errorText).isNotEqualTo("")
        viewModel.clearErrorText()
        assertThat(viewModel.uiState.value.errorText).isEqualTo("")
    }

    // ● changeHiraKanaType --------------------------------------------------------------------- ●
    @Test
    fun convertViewModel_updateType_isChangedAndClearPreviousInputText() = runTest {
        // ひらがなカタカナを変更するとpreviousInputTextを初期化させるため、
        // 変換後にinputTextを変更しなくても変換できることを確認
        viewModelNotReturnErrorResponse.let { viewModel ->
            viewModel.updateInputText("お")
            viewModel.convert(context = context)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
            assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
            viewModel.changeHiraKanaType(type = HiraKanaType.KATAKANA)
            assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.KATAKANA)
            viewModel.updateOutputText("")
            assertThat(viewModel.uiState.value.outputText).isEqualTo("")
            viewModel.convert(context = context)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.outputText).isNotEqualTo("")
        }
    }
}

private fun createTestingConvertViewModel(
    context: Context,
    testDispatcher: CoroutineDispatcher,
    isErrorResponse: Boolean,
    isReachedConvertMaxLimit: Boolean,
): ConvertViewModelImpl {
    return ConvertViewModelImpl(
        convertRepository = FakeConverterRepository(context, isErrorResponse),
        dataStoreRepository = FakeDataStoreRepositoryImpl(isReachedConvertMaxLimit),
        convertHistoryRepository = FakeConvertHistoryRepositoryImpl(),
        ioDispatcher = testDispatcher,
        defaultDispatcher = testDispatcher,
    )
}

@OptIn(ExperimentalSerializationApi::class)
private class FakeConverterRepository(
    context: Context,
    isErrorResponse: Boolean,
) : ConvertRepository {
    private val contentType = "application/json".toMediaType()
    private val fakeInterceptor = FakeInterceptor(
        isErrorResponse = isErrorResponse,
        context = context,
    )
    private val client = OkHttpClient.Builder().addInterceptor(fakeInterceptor).build()
    private val convertService = Retrofit.Builder()
        .baseUrl("https://testing")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()
        .create(ConvertApiClient::class.java)

    override suspend fun requestConvert(
        sentence: String,
        type: String,
        appId: String,
    ): retrofit2.Response<ResponseData> {
        val requestData = RequestData(
            appId = appId,
            sentence = sentence,
            outputType = type,
        )
        val json = Json.encodeToString(requestData)
        val body = json.toRequestBody(contentType)
        return runBlocking { convertService.requestConvert(body) }
    }
}

private class FakeInterceptor(
    private val isErrorResponse: Boolean,
    private val context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return Response
            .Builder()
            .code(code = if (isErrorResponse) 400 else 200)
            .protocol(Protocol.HTTP_2)
            .body(
                Json.encodeToString(
                    ResponseData(
                        requestId = "",
                        outputType = "Hiragana",
                        converted = "えー",
                    ),
                ).toResponseBody("application/json".toMediaType()),
            )
            .message(
                message = if (isErrorResponse) {
                    context.getString(R.string.network_error)
                } else {
                    "OK"
                },
            )
            .request(request)
            .build()
    }
}

private class FakeConvertHistoryRepositoryImpl : ConvertHistoryRepository {
    override fun insertConvertHistory(beforeText: String, afterText: String, time: String) {}
    override fun getAllConvertHistory(): List<ConvertHistoryData> = emptyList()
    override fun deleteAllConvertHistory() {}
    override fun deleteConvertHistory(id: Long) {}
}

private class FakeDataStoreRepositoryImpl(
    private val isReachedConvertMaxLimit: Boolean,
) : DataStoreRepository {
    override fun selectedThemeNum(): Flow<Int> = flow { }
    override fun selectedCustomFont(): Flow<String> = flow { }
    override suspend fun updateThemeNum(newThemeNum: Int) {}
    override suspend fun updateCustomFont(newCustomFont: CustomFont) {}
    override suspend fun checkReachedConvertMaxLimit(today: String) = isReachedConvertMaxLimit
}
