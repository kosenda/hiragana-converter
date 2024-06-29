import com.google.firebase.perf.plugin.FirebasePerfExtension
import ksnd.hiraganaconverter.kotlinOptions
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("hiraganaconverter.android.application.jacoco")
    id("hiraganaconverter.android.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets)
    alias(libs.plugins.dokka)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.aboutLibraries)
}

android {
    namespace = "ksnd.hiraganaconverter"
    compileSdk = 34
    defaultConfig {
        applicationId = "ksnd.hiraganaconverter"
        minSdk = 26
        targetSdk = 34
        versionCode = 43
        versionName = "1.32"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    androidResources {
        generateLocaleConfig = true
    }

    // ref: https://github.com/DroidKaigi/conference-app-2023/blob/main/app-android/build.gradle.kts
    val keystorePropertiesFile = file("keystore.properties")
    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
                storeFile = keystoreProperties["storeFile"]?.let { file(it) }
                storePassword = keystoreProperties["storePassword"] as String?
            }
        }
    }
    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro", "shrinker-rules.pro")
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            firebaseAppDistribution {
                artifactType = "apk"
                groups="tester"
                serviceCredentialsFile = "firebase-app-distribution.json"
            }
        }
        debug {
            configure<FirebasePerfExtension> {
                setInstrumentationEnabled(false)
            }
        }
    }
    flavorDimensions += "env"
    productFlavors {
        create("prod") {
            isDefault = true
            dimension = "env"
        }
        create("mock") {
            dimension = "env"
            applicationIdSuffix = ".mock"
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeCompiler {
        enableStrongSkippingMode = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(projects.feature.converter)
    implementation(projects.feature.history)
    implementation(projects.feature.info)
    implementation(projects.feature.setting)
    implementation(projects.core.analytics)
    implementation(projects.core.model)
    implementation(projects.core.resource)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.ui)
    testImplementation(projects.core.testing)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.google.fonts)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Lottie
    implementation(libs.lottie)

    // Timber
    implementation(libs.timber)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)

    // App Update
    implementation(libs.app.update)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Showkase
    debugImplementation(libs.showkase)
    implementation(libs.showkase.annotation)
    kspDebug(libs.showkase.processor)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // AboutLibraries
    implementation(libs.aboutLibraries)
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}
