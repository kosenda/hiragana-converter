package ksnd.hiraganaconverter.core.network

import ksnd.hiraganaconverter.core.model.ResponseData
import retrofit2.Response

interface ConvertApiClient {
    suspend fun requestConvert(appId: String, sentence: String, type: String): Response<ResponseData>
}
