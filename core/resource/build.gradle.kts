plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.core.resource"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
}