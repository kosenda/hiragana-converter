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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:resource"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    testImplementation(project(":core:testing"))
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // App Review
    implementation(libs.app.review)
    implementation(libs.app.review.ktx)

    // Analytics
    implementation(project(":core:analytics"))
}
