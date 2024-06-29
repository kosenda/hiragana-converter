plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.library.compose")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.feature.setting"
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
    implementation(projects.core.ui)
    implementation(projects.core.resource)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    testImplementation(projects.core.testing)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.appcompat)
}
