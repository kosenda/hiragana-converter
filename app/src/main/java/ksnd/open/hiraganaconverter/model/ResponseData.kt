package ksnd.open.hiraganaconverter.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(

    /** リクエストID */
    @SerialName("request_id")
    val requestId: String,

    /** 出力タイプ */
    @SerialName("output_type")
    val outputType: String,

    /** 変換後文字列 */
    @SerialName("converted")
    val converted: String,
)
