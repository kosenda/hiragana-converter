package ksnd.open.hiraganaconverter.model.repository

import ksnd.open.hiraganaconverter.model.ResponseData
import retrofit2.Response

interface ConvertRepository {
    suspend fun requestConvert(sentence: String, type: String, appId: String): Response<ResponseData>?
}
