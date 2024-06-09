plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.library.compose")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.feature.history"
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
    implementation(projects.core.domain)
    implementation(projects.core.resource)
    implementation(projects.core.ui)
    testImplementation(projects.core.testing)

    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.lazyColumnScrollbar)
}
