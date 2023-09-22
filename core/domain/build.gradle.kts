plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:resource"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
}
