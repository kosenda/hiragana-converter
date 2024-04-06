import com.google.firebase.perf.plugin.FirebasePerfExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("hiraganaconverter.android.application")
    id("hiraganaconverter.android.application.jacoco")
    id("hiraganaconverter.android.hilt")
    alias(libs.plugins.oss.licenses)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets)
    alias(libs.plugins.dokka)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.firebase.appdistribution)
}

android {
    namespace = "ksnd.hiraganaconverter"

    androidResources {
        generateLocaleConfig = true
    }

    // ref: https://github.com/DroidKaigi/conference-app-2023/blob/main/app-android/build.gradle.kts
    val keystorePropertiesFile = file("../keystore.properties")
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
            firebaseAppDistribution {
                artifactType = "apk"
                releaseNotes = "test"
                groups="tester"
            }
            signingConfig = signingConfigs.getByName("release")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
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
    implementation(project(":feature:converter"))
    implementation(project(":feature:history"))
    implementation(project(":feature:info"))
    implementation(project(":feature:setting"))
    implementation(project(":core:analytics"))
    implementation(project(":core:model"))
    implementation(project(":core:resource"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    testImplementation(project(":core:testing"))

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

    // OSS Licenses
    implementation(libs.play.oss.licenses)

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
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}
