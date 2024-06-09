pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
rootProject.name = "hiraganaconverter"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
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
