package vlad.chetrari.bvtesttask.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
//TODO plant crashlytics tree if production (inject tree)
        Timber.plant(Timber.DebugTree())
    }
}