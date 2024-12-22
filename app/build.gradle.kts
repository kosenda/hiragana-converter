import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.google.firebase.perf.plugin.FirebasePerfExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
    compileSdk = 35
    defaultConfig {
        applicationId = "ksnd.hiraganaconverter"
        minSdk = 26
        targetSdk = 35
        versionCode = 46
        versionName = "1.35"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
        unitTests.all {
            it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
        }
    }
}

roborazzi {
    @OptIn(ExperimentalRoborazziApi::class)
    generateComposePreviewRobolectricTests {
        enable = true
        testerQualifiedClassName = "ksnd.hiraganaconverter.RoborazziComposePreviewTest"
        packages = listOf("ksnd.hiraganaconverter")
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

    // Supported: Workaround for AGP not merging test manifest
    //   ref: https://github.com/robolectric/robolectric/pull/4736
    debugImplementation(libs.androidx.compose.ui.test.manifest)

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

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // AboutLibraries
    implementation(libs.aboutLibraries)

    // Roborazzi (for ComposablePreviewScanner)
    testImplementation(libs.roborazzi.compose.preview.scanner.support)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.composable.preview.scanner)
    testImplementation(libs.webp.image.io)
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}
