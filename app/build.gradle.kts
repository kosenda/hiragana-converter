@file:Suppress("UnstableApiUsage")

val ktlint: Configuration by configurations.creating

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.google.android.gms.oss-licenses-plugin")
    jacoco
}
jacoco {
    toolVersion = "0.8.9"
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
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

    // Compose
    implementation(libs.androidx.compose.ui)

    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")

    // Material you
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.compose.material3:material3:1.2.0-alpha03")

    // accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    // COIL
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // dataStore preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ktlint
    ktlint("com.pinterest:ktlint:0.48.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // ROOM
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // Robolectric environment
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.robolectric:robolectric:4.10.3")

    // kotlinx serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Kotlin Coroutine Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")

    // Truth
    testImplementation("com.google.truth:truth:1.1.5")
    androidTestImplementation("com.google.truth:truth:1.1.5")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // OSS Licenses Gradle Plugin
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")
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