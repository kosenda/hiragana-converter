package ksnd.hiraganaconverter.core.resource

data class AppConfig(
    val apiKey: String,
    val applicationId: String,
    val buildType: String,
    val isDebug: Boolean,
    val versionCode: Int,
    val versionName: String,
)
