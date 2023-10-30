plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.compose")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.feature.info"
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
    implementation(project(":core:analytics"))
    implementation(project(":core:ui"))
    implementation(project(":core:resource"))
    implementation(project(":core:model"))
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.webView)
    implementation(libs.play.oss.licenses)
}
