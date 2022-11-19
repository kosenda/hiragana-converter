package ksnd.open.hiraganaconverter.model.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ksnd.open.hiraganaconverter.model.ConvertApiClient
import ksnd.open.hiraganaconverter.model.ResponseData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ConvertRepositoryImpl : ConvertRepository {

    // JSONからKotlinのクラスに変換するためのライブラリの設定
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // REST APIを利用するためのライブラリの設定
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://labs.goo.ne.jp/api/hiragana/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // ApiClientに定義したメソッドを呼び出すための設定
    private val convertService = retrofit.create(ConvertApiClient::class.java)

    // APIからデータを取得するメソッド
    override suspend fun requestConvert(
        sentence: String,
        type: String,
        appId: String
    ): Response<ResponseData>? {
        return try {
            val json = JSONObject()
                .put("app_id", appId)
                .put("sentence", sentence)
                .put("output_type", type)
                .toString()
            Log.i("json: ", json)
            val body = json.toRequestBody(
                contentType = "application/json; charset=utf-8".toMediaTypeOrNull()
            )
            convertService.requestConvert(body)
        } catch (e: Exception) {
            Log.e("request_convert", e.toString())
            null
        }
    }
}
