package ksnd.hiraganaconverter.core.ui

import android.os.Build

// ref: https://github.com/DroidKaigi/conference-app-2023/blob/f255ed2f6f07f9f6f83bc3b15384b9bcf001d8e8/core/ui/src/androidMain/kotlin/io/github/droidkaigi/confsched2023/ui/TestEnviroment.kt
fun isTest(): Boolean {
    return "robolectric" == Build.FINGERPRINT
}
