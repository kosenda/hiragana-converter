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
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:resource"))
    implementation(project(":core:ui"))
    testImplementation(project(":core:testing"))
    implementation(libs.timber)
}