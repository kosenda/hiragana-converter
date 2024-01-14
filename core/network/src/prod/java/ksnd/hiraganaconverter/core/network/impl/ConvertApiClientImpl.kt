package ksnd.hiraganaconverter.core.network.impl

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ksnd.hiraganaconverter.core.model.RequestData
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.network.ConvertApi
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import ksnd.hiraganaconverter.core.network.interceptor.ErrorInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class ConvertApiClientImpl @Inject constructor(
    errorInterceptor: ErrorInterceptor,
) : ConvertApiClient {
    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClient.Builder().addInterceptor(errorInterceptor).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://labs.goo.ne.jp/api/hiragana/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()

    private val convertService = retrofit.create(ConvertApi::class.java)

    override suspend fun requestConvert(appId: String, sentence: String, type: String): Response<ResponseData> {
        val requestData = RequestData(
            appId = appId,
            sentence = sentence,
            outputType = type,
        )
        val json = Json.encodeToString(requestData)
        val body = json.toRequestBody(contentType = contentType)
        val response = convertService.requestConvert(body)
        if (response.isSuccessful.not()) {
            Timber.w("response message: %s".format(response.raw()))
        }
        Timber.i("response raw: %s".format(response.raw()))
        return response
    }
}
