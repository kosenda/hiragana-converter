package ksnd.hiraganaconverter.view

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ksnd.hiraganaconverter.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(tree = Timber.DebugTree())
    }
}
