plugins {
    // apply false: only load
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.oss.licenses) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.androidLibrary) apply false
}

tasks.create<JacocoReport>("mergeJacoco") {
    val testTaskName = "testProdDebugUnitTest"
    reports {
        html.required.set(true)
        xml.required.set(true)
    }

    if (project.rootProject != project && project.plugins.hasPlugin("jacoco")) {
        gradle.afterProject {
            executionData.from.add(fileTree("${layout.buildDirectory.get()}/jacoco/$testTaskName.exec"))
            sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
            classDirectories.setFrom(
                fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/prodDebug") {
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
                        "**/*ConvertTextUseCaseError*.*",
                        "**/*ResponseData*.*",
                        "**/*RequestData*.*",
                        "**/*ErrorInterceptor*.*",
                        "**/*Application*.*",
                        "**/view/**",
                        "**/mock/**",
                        "**/*Mock*.*",
                    )
                }
            )
        }
    }
}
