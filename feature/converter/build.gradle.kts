plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.library.compose")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.feature.converter"
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
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.resource)
    implementation(projects.core.ui)
    implementation(projects.core.analytics)
    testImplementation(projects.core.testing)

    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.lazyColumnScrollbar)
    implementation(libs.firebase.perf)
    implementation(libs.androidx.navigation.compose)
}
