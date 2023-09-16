package kosenda.makecolor

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/*
 * ref: https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/KotlinAndroid.kt
 */
fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

/*
 * ref: https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/Jacoco.kt
 */
internal fun Project.configureJacoco() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }
    val jacocoTestReport = tasks.create("jacocoTestReport")
    val testTaskName = "testProdDebugUnitTest"
    val reportTask = tasks.register(
        name = "jacoco${testTaskName}Report",
        type = JacocoReport::class
    ) {
        dependsOn(testTaskName)
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
        classDirectories.setFrom(fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/prodDebug"))
        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        executionData.from.add(fileTree("${layout.buildDirectory.get()}/jacoco/$testTaskName.exec"))
    }
    jacocoTestReport.dependsOn(reportTask)
    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }
}
