package ksnd.open.hiraganaconverter.model.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ksnd.open.hiraganaconverter.model.ConvertApiClient
import ksnd.open.hiraganaconverter.model.RequestData
import ksnd.open.hiraganaconverter.model.ResponseData
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class ConvertRepositoryImpl @Inject constructor(
    errorInterceptor: Interceptor,
) : ConvertRepository {

    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClient.Builder().addInterceptor(errorInterceptor).build()

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://labs.goo.ne.jp/api/hiragana/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()

    private val convertService = retrofit.create(ConvertApiClient::class.java)

    override suspend fun requestConvert(
        sentence: String,
        type: String,
        appId: String,
    ): Response<ResponseData>? {
        try {
            val requestData = RequestData(
                appId = appId,
                sentence = sentence,
                outputType = type,
            )
            val json = Json.encodeToString(requestData)
            Timber.i("json: %s", json)
            val body = json.toRequestBody(
                contentType = "application/json; charset=utf-8".toMediaTypeOrNull(),
            )
            val response: Response<ResponseData> = convertService.requestConvert(body)
            if (response.isSuccessful.not()) {
                Timber.w("response_message: %s", response.raw().message)
            }
            Timber.i("response raw: %s" + response.raw())
            return response
        } catch (e: Exception) {
            // 基本的にはAPI通信に関してはErrorInterceptorでcatchされ失敗してもレスポンスが返ってくる想定であり、
            // ここのcatchはいらないとは思うが念のためここでもエラー処理を行う
            Timber.e(e)
            return null
        }
    }
}
