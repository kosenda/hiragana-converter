buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.3.1'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10'
        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.43.2'
        classpath 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1'
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}