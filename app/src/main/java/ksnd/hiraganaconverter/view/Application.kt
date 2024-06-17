package ksnd.hiraganaconverter.view

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.HiltAndroidApp
import ksnd.hiraganaconverter.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(tree = if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
    }
}

private class ReleaseTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        val priorityStr = when (priority) {
            Log.ERROR -> "E/"
            Log.WARN -> "W/"
            else -> return // Ignore other log levels
        }
        Firebase.crashlytics.log(
            "%s%s%s".format(
                priorityStr,
                tag?.let { "[$it]" } ?: "",
                message,
            ),
        )
        t?.let { Firebase.crashlytics.recordException(it) }
    }
}
