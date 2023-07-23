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
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}