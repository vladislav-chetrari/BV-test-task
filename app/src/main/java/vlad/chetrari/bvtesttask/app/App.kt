package vlad.chetrari.bvtesttask.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import vlad.chetrari.bvtesttask.BuildConfig

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}