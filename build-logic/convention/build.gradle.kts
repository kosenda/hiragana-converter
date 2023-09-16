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
        register("androidApplication") {
            id = "hiraganaconverter.android.application"
            implementationClass = "ksnd.hiraganaconverter.AndroidApplicationPlugin"
        }
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
    }
}
