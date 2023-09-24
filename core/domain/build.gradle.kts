plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ksnd.hiraganaconverter.core.domain"
    flavorDimensions += "env"
    productFlavors {
        create("prod") {
            dimension = "env"
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:resource"))
    testImplementation(project(":core:testing"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)
}