import java.net.URL

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.google.android.gms.oss-licenses-plugin" -> useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = URL("https://jitpack.io"))
    }
}
rootProject.name = "hiraganaconverter"
include(":app")
include(":feature:converter")
include(":feature:history")
include(":feature:info")
include(":feature:setting")
include(":core:model")
include(":core:resource")
include(":core:domain")
include(":core:data")
include(":core:testing")
include(":core:ui")
include(":core:analytics")
include(":core:network")
