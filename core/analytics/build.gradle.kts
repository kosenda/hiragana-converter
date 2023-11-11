plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.timber)
}
