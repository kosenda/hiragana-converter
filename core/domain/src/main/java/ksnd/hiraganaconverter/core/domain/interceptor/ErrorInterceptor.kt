package ksnd.hiraganaconverter.core.domain.interceptor

import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ErrorInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)

            if (response.isSuccessful) {
                return response
            }

            Timber.w("beforeConvertedErrorMessage: %s".format(response.message))
            val convertedMessage = when (response.code) {
                413 -> ConvertErrorType.TOO_MANY_CHARACTER.name
                400 -> when (response.message) {
                    "Rate limit exceeded" -> ConvertErrorType.RATE_LIMIT_EXCEEDED.name
                    else -> ConvertErrorType.CONVERSION_FAILED.name
                }
                500 -> ConvertErrorType.INTERNAL_SERVER.name
                else -> ConvertErrorType.CONVERSION_FAILED.name
            }
            return response.newBuilder().message(message = convertedMessage).build()
        } catch (e: Exception) {
            Timber.e(e)
            val message = if (e is IOException) ConvertErrorType.NETWORK.name else ConvertErrorType.CONVERSION_FAILED.name
            return Response
                .Builder()
                .request(request)
                .code(0)
                .protocol(Protocol.HTTP_2)
                .body("".toResponseBody("application/json".toMediaType()))
                .message(message = message)
                .build()
        }
    }
}
