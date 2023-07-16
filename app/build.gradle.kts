@file:Suppress("UnstableApiUsage")

val ktlint: Configuration by configurations.creating

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.oss.licenses)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets)
    jacoco
}
jacoco {
    toolVersion = libs.versions.jacoco.get()
}

android {
    namespace = "ksnd.hiraganaconverter"
    compileSdk = 34

    defaultConfig {
        applicationId = "ksnd.hiraganaconverter"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true // リソースの圧縮
            isMinifyEnabled = true   // コードの圧縮
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro", "shrinker-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
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
        // 以下のjvmArgsを指定しないとカバレッジが取得できないぽい
        // 参考サイト https://github.com/robolectric/robolectric/issues/5428
        // noverifyは、全てのバイトコードの検証を無効にするオプションぽい
        // 参考サイト https://docs.oracle.com/javase/jp/8/docs/technotes/tools/windows/java.html
        unitTests.all { it.jvmArgs("-noverify") }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.junit)

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation("com.google.android.material:material:1.9.0")

    // COIL
    implementation(libs.coil)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // dataStore preferences
    implementation(libs.androidx.dataStore.preferences)

    // ktlint
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // ROOM
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Robolectric environment
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlin Coroutine Test
    testImplementation(libs.kotlinx.coroutines.test)

    // Truth
    testImplementation(libs.truth)

    // Timber
    implementation(libs.timber)

    // OSS Licenses
    implementation(libs.play.oss.licenses)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
}

// チェック
tasks.create<JavaExec>("ktlintCheck") {
    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf(
        "src/**/*.kt",
        "--reporter=checkstyle,output=${buildDir}/reports/ktlint/ktlint-result.xml",
    )
    isIgnoreExitValue = true
}

// フォーマット
// jvmArgsは以下のサイトを参考にした（入れないと失敗してしまう）
// https://github.com/pinterest/ktlint/issues/1195#issuecomment-1009027802
tasks.create<JavaExec>("ktlintFormatting") {
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args("-F", "src/**/*.kt")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

tasks.create<JacocoReport>("jacocoTestReport") {
    val testTaskName = "testDebugUnitTest"
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
    gradle.afterProject {
        executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        classDirectories.setFrom(
            fileTree("$buildDir/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class",
                    "**/R\$*.class",
                    "**/*Fake*.*",
                    "**/*Preview*.*",
                    "**/BuildConfig.*",
                    "**/*Manifest*.*",
                    "**/*Test*.*",
                    "**/*Hilt*.*",
                    "**/*Factory*.*",
                    "**/*Module*.*",
                    "**/*Key*.*",
                    "**/*Screen*.*",
                    "**/*Content*.*",
                    "**/*Dialog*.*",
                    "**/*Drawer*.*",
                    "**/*Navigation*.*",
                    "**/*MainActivity*.*",
                    "**/view/**",
                )
            }
        )
    }
}