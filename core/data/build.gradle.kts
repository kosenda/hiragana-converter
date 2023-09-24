plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.library.jacoco")
    id("hiraganaconverter.android.hilt")
    id("hiraganaconverter.android.room")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ksnd.hiraganaconverter.core.data"

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
    sourceSets {
        getByName("prod") {
            java.srcDirs("src/prod/java")
        }
        getByName("mock") {
            java.srcDirs("src/mock/java")
        }
    }
}

dependencies {
    implementation(project(":core:resource"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    testImplementation(project(":core:testing"))
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    // App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
}
