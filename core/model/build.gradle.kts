plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.room")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ksnd.hiraganaconverter.core.model"
}

dependencies {
    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)
}
