package ksnd.hiraganaconverter.model

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
            when (response.code) {
                413 -> {
                    return response
                        .newBuilder()
                        .message(message = ConvertErrorType.TOO_MANY_CHARACTER.name)
                        .build()
                }
                400 -> {
                    val convertedMessage = when (response.message) {
                        "Rate limit exceeded" -> ConvertErrorType.RATE_LIMIT_EXCEEDED.name
                        else -> ConvertErrorType.CONVERSION_FAILED.name
                    }
                    return response.newBuilder().message(message = convertedMessage).build()
                }
                500 -> {
                    return response
                        .newBuilder()
                        .message(message = ConvertErrorType.INTERNAL_SERVER.name)
                        .build()
                }
                else -> {
                    return response
                        .newBuilder()
                        .message(message = ConvertErrorType.CONVERSION_FAILED.name)
                        .build()
                }
            }
        } catch (ioe: IOException) {
            Timber.w("NetworkError: %s".format(ioe))
            return Response
                .Builder()
                .request(request)
                .code(0)
                .protocol(Protocol.HTTP_2)
                .body("".toResponseBody("application/json".toMediaType()))
                .message(message = ConvertErrorType.NETWORK.name)
                .build()
        } catch (e: Exception) {
            Timber.e(e)
            return Response
                .Builder()
                .request(request)
                .code(0)
                .protocol(Protocol.HTTP_2)
                .body("".toResponseBody("application/json".toMediaType()))
                .message(message = ConvertErrorType.CONVERSION_FAILED.name)
                .build()
        }
    }
}
