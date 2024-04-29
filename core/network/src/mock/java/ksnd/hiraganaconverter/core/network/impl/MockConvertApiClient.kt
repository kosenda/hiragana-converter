package ksnd.hiraganaconverter.core.network.impl

import kotlinx.coroutines.delay
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import ksnd.hiraganaconverter.core.network.mock.MockConverted
import retrofit2.Response
import javax.inject.Inject

class MockConvertApiClient @Inject constructor() : ConvertApiClient {
    override suspend fun requestConvert(appId: String, sentence: String, type: String): Response<ResponseData> {
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
