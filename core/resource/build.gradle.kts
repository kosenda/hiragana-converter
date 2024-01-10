plugins {
    id("hiraganaconverter.android.library")
    id("hiraganaconverter.android.hilt")
}

android {
    namespace = "ksnd.hiraganaconverter.core.resource"
    flavorDimensions += "env"
    productFlavors {
        create("prod") {
            isDefault = true
            dimension = "env"
        }
        create("mock") {
            dimension = "env"
        }
    }
    sourceSets {
        getByName("prod") {
            res.srcDirs("src/prod/res")
        }
        getByName("mock") {
            res.srcDirs("src/mock/res")
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
}
