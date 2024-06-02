plugins {
    `kotlin-dsl`
}

group = "ksnd.hiraganaconverter.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationJacoco") {
            id = "hiraganaconverter.android.application.jacoco"
            implementationClass = "ksnd.hiraganaconverter.AndroidApplicationJacocoPlugin"
        }
        register("androidLibraryJacoco") {
            id = "hiraganaconverter.android.library.jacoco"
            implementationClass = "ksnd.hiraganaconverter.AndroidLibraryJacocoPlugin"
        }
        register("androidLibrary") {
            id = "hiraganaconverter.android.library"
            implementationClass = "ksnd.hiraganaconverter.AndroidLibraryPlugin"
        }
        register("androidRoom") {
            id = "hiraganaconverter.android.room"
            implementationClass = "ksnd.hiraganaconverter.AndroidRoomPlugin"
        }
        register("androidHilt") {
            id = "hiraganaconverter.android.hilt"
            implementationClass = "ksnd.hiraganaconverter.AndroidHiltPlugin"
        }
        register("androidLibraryCompose") {
            id = "hiraganaconverter.android.library.compose"
            implementationClass = "ksnd.hiraganaconverter.AndroidLibraryComposePlugin"
        }
    }
}
