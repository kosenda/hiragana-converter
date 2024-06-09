plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.core.domain"
    flavorDimensions += "env"
    productFlavors {
        create("prod") {
            isDefault = true
            dimension = "env"
        }
        create("mock") {
            dimension = "env"
        }
    }
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.model)
    implementation(projects.core.resource)
    testImplementation(projects.core.testing)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.timber)
    implementation(libs.app.update)
    implementation(libs.kotlinx.datetime)
}
