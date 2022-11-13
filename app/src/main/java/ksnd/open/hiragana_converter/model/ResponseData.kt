package ksnd.open.hiragana_converter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ResponseData(

    /** リクエストID */
    val request_id: String,

    /** 出力タイプ */
    val output_type: String,

    /** 変換後文字列 */
    val converted: String
)
