@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    jacoco
}
jacoco {
    toolVersion = libs.versions.jacoco.get()
}

android {
    namespace = "ksnd.hiraganaconverter.core.model"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)
}

tasks.create<JacocoReport>("jacocoTestReport") {
    val testTaskName = "testDebugUnitTest"
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
    executionData.from.add(fileTree("${layout.buildDirectory.get()}/jacoco/$testTaskName.exec"))
    sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
    classDirectories.setFrom(fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/prodDebug"))
}
