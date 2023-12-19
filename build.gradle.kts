plugins {
    // apply false: only load
    jacoco
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
    alias(libs.plugins.firebase.perf) apply false
}

tasks.create<JacocoReport>("jacocoTestReport") {
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
    gradle.afterProject {
        if (project.rootProject != this.project &&
            (project.plugins.hasPlugin("hiraganaconverter.android.application.jacoco") ||
                project.plugins.hasPlugin("hiraganaconverter.android.library.jacoco")
            )
        ) {
            executionData.from.add(fileTree("${project.layout.buildDirectory.get()}/jacoco"))
            sourceDirectories.from.add(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
            classDirectories.from.add(
                fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/prodDebug") {
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
                        "**/*MainActivity.*",
                        "**/*ConvertTextUseCaseError*.*",
                        "**/*ResponseData*.*",
                        "**/*RequestData*.*",
                        "**/*ErrorInterceptor*.*",
                        "**/*Application*.*",
                        "**/view/**",
                        "**/mock/**",
                        "**/*Mock*.*",
                        "**/*Navigation*.*",
                    )
                }
            )
        }
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
        }
    }
}
