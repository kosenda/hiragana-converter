plugins {
    id("hiraganaconverter.android.library")
}

android {
    namespace = "ksnd.hiraganaconverter.core.testing"
}

dependencies {
    api(libs.kotlin.test)
    api(libs.androidx.compose.ui.test.junit4)

    // Robolectric environment
    api(libs.androidx.test.core)
    api(libs.robolectric)

    // Kotlin Coroutine Test
    api(libs.kotlinx.coroutines.test)

    // Truth
    api(libs.truth)

    // Turbine
    api(libs.turbine)

    // Mockk
    api(libs.mockk)

    // Roborazzi
    api(libs.roborazzi)
    api(libs.roborazzi.compose)
    api(libs.roborazzi.junit4.rule)
}
