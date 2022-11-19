package ksnd.open.hiragana_converter.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ResponseData(

    /** リクエストID */
    @Json(name = "request_id")
    val requestId: String,

    /** 出力タイプ */
    @Json(name = "output_type")
    val outputType: String,

    /** 変換後文字列 */
    @Json(name = "converted")
    val converted: String
)
