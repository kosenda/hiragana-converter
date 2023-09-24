plugins {
    id("hiraganaconverter.android.library")
}

android {
    namespace = "ksnd.hiraganaconverter.core.ui"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:resource"))

    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.google.fonts)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // COIL
    implementation(libs.coil)

    // Lottie
    implementation(libs.lottie)
}
