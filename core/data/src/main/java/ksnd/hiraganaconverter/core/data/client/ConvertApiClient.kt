package ksnd.hiraganaconverter.core.data.client

import ksnd.hiraganaconverter.core.model.ResponseData
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ConvertApiClient {

    @POST(" ")
    suspend fun requestConvert(@Body body: RequestBody): Response<ResponseData>
}