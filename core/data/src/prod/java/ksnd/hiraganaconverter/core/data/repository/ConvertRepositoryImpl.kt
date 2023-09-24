package ksnd.hiraganaconverter.core.data.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.serialization.json.Json
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.model.RequestData
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.data.client.ConvertApiClient
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class ConvertRepositoryImpl @Inject constructor(
    errorInterceptor: Interceptor,
) : ConvertRepository {

    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClient.Builder().addInterceptor(errorInterceptor).build()

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
            Timber.i("json: %s".format(json))
            val body = json.toRequestBody(
                contentType = "application/json; charset=utf-8".toMediaTypeOrNull(),
            )
            val response: Response<ResponseData> = convertService.requestConvert(body)
            if (response.isSuccessful.not()) {
                Timber.w("response message: %s".format(response.raw()))
            }
            Timber.i("response raw: %s".format(response.raw()))
            return response
        } catch (e: Exception) {
            /*
             * Basically, it is assumed that API communication is caught by ErrorInterceptor and a response is returned even if it fails,
             * and error processing is also performed here just in case, although it is not necessary to catch here.
             */
            Timber.e(e)
            return null
        }
    }
}
