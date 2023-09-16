package ksnd.hiraganaconverter

import kosenda.makecolor.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryJacocoPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.gradle.jacoco")
                apply("com.android.library")
            }
            configureJacoco()
        }
    }
}
