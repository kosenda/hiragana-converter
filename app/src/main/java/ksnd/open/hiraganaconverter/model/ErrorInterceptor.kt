package ksnd.open.hiraganaconverter.model

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ksnd.open.hiraganaconverter.R
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    private val tag = ErrorInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)

            if (response.isSuccessful) {
                return response
            }

            // 変換に失敗していた場合に、レスポンスコードによってメッセージを変換する
            Log.w(tag, "beforeConvertedErrorMessage: ${response.message}")
            when (response.code) {
                413 -> {
                    return response
                        .newBuilder()
                        .message(message = context.getString(R.string.request_too_large))
                        .build()
                }
                400 -> {
                    val convertedMessage = when (response.message) {
                        "Rate limit exceeded" -> context.getString(R.string.limit_exceeded)
                        else -> context.getString(R.string.conversion_failed)
                    }
                    return response.newBuilder().message(message = convertedMessage).build()
                }
                500 -> {
                    return response
                        .newBuilder()
                        .message(message = context.getString(R.string.internal_server_error))
                        .build()
                }
                else -> {
                    return response
                        .newBuilder()
                        .message(message = context.getString(R.string.conversion_failed))
                        .build()
                }
            }
        } catch (ioe: IOException) {
            Log.w(tag, "ネットワークエラー: $ioe")
            return Response
                .Builder()
                .request(request)
                .code(0)
                .protocol(Protocol.HTTP_2)
                .body("".toResponseBody("application/json".toMediaType()))
                .message(message = context.getString(R.string.network_error))
                .build()
        } catch (e: Exception) {
            Log.e(tag, e.toString())
            return Response
                .Builder()
                .request(request)
                .code(0)
                .protocol(Protocol.HTTP_2)
                .body("".toResponseBody("application/json".toMediaType()))
                .message(message = context.getString(R.string.conversion_failed))
                .build()
        }
    }
}
