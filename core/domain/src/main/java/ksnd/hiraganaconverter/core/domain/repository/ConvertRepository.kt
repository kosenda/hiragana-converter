package ksnd.hiraganaconverter.core.domain.repository

import ksnd.hiraganaconverter.core.model.ResponseData
import retrofit2.Response

interface ConvertRepository {
    suspend fun requestConvert(sentence: String, type: String, appId: String): Response<ResponseData>?
}
