plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ksnd.hiraganaconverter.core.ui"
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
    implementation(projects.core.resource)

    // COIL
    implementation(libs.coil)

    // Lottie
    implementation(libs.lottie)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // AboutLibraries
    implementation(libs.aboutLibraries)
}
