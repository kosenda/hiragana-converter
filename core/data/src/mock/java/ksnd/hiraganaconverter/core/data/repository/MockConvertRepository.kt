package ksnd.hiraganaconverter.core.data.repository

import kotlinx.coroutines.delay
import ksnd.hiraganaconverter.core.data.mock.MockConverted
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import retrofit2.Response
import javax.inject.Inject

class MockConvertRepository @Inject constructor() : Convertepository {
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
