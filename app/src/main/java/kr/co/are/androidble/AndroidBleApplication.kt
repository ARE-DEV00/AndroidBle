package kr.co.are.androidble

import android.app.Application
import timber.log.Timber


class AndroidBleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}