package ksnd.hiraganaconverter

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.gradle.jacoco")
                apply("com.android.application")
            }
            configureJacoco()
        }
    }
}
