package ksnd.open.hiraganaconverter.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class RequestData(

    /** アプリケーションID */
    @Json(name = "app_id")
    val appId: String,

    /** 解析対象テキスト */
    @Json(name = "sentence")
    val sentence: String,

    /** 出力タイプ */
    @Json(name = "output_type")
    val outputType: String
)
