plugins {
    // apply false: only load
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.oss.licenses) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.secrets) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
    }
}