package ksnd.hiraganaconverter.model.repository

import kotlinx.coroutines.delay
import ksnd.hiraganaconverter.mock.data.MockConverted
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.model.ResponseData
import retrofit2.Response
import javax.inject.Inject

class MockConvertRepository @Inject constructor() : ConvertRepository {
    override suspend fun requestConvert(
        sentence: String,
        type: String,
        appId: String,
    ): Response<ResponseData>? {
        delay(1000)
        return Response.success(
            200,
            ResponseData(
                requestId = appId,
                outputType = type,
                converted = if (type == HiraKanaType.HIRAGANA.name.lowercase()) MockConverted().hiragana else MockConverted().katakana,
            ),
        )
    }
}
