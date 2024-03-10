package ksnd.hiraganaconverter.core.data.repository

import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.model.ResponseData
import ksnd.hiraganaconverter.core.network.ConvertApiClient
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class ConvertRepositoryImpl @Inject constructor(
    private val convertApiClient: ConvertApiClient,
) : ConvertRepository {

    override suspend fun requestConvert(
        sentence: String,
        type: String,
        appId: String,
    ): Response<ResponseData>? {
        return try {
            convertApiClient.requestConvert(appId = appId, sentence = sentence, type = type)
        } catch (e: Exception) {
            /*
             * Basically, it is assumed that API communication is caught by ErrorInterceptor and a response is returned even if it fails,
             * and error processing is also performed here just in case, although it is not necessary to catch here.
             */
            Timber.e(e)
            null
        }
    }
}
