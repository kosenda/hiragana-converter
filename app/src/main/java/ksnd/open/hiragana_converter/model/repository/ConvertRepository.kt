package ksnd.open.hiragana_converter.model.repository

import ksnd.open.hiragana_converter.model.ResponseData
import retrofit2.Response

interface ConvertRepository {
    suspend fun requestConvert(sentence: String, type: String, appId: String): Response<ResponseData>?
}
