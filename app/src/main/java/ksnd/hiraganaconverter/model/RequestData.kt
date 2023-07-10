package ksnd.hiraganaconverter.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestData(

    /** アプリケーションID */
    @SerialName("app_id")
    val appId: String,

    /** 解析対象テキスト */
    @SerialName("sentence")
    val sentence: String,

    /** 出力タイプ */
    @SerialName("output_type")
    val outputType: String,
)
