val ktlint: Configuration by configurations.creating

plugins {
    // apply false: only load
    jacoco
    alias(libs.plugins.squareup.invert)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.firebase.appdistribution) apply false
    alias(libs.plugins.aboutLibraries) apply false
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
                        "**/*State*.*",
                    )
                }
            )
        }
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            val composeCompilerDir = "${project.layout.buildDirectory.get()}/compose_compiler"
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs.add("-P=plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$composeCompilerDir")
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs.add("-P=plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$composeCompilerDir")
            }
        }
    }
}

dependencies {
    // ktlint
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

tasks.create<JavaExec>("ktlintCheck") {
    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf(
        "**src/**/*.kt",
        "--reporter=checkstyle,output=${layout.buildDirectory.get()}/reports/ktlint/ktlint-result.xml",
    )
    isIgnoreExitValue = true
}

tasks.create<JavaExec>("ktlintFormatting") {
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args("-F", "**src/**/*.kt")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
