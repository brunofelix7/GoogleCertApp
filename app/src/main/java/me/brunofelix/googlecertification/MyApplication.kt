package me.brunofelix.googlecertification

import android.app.Application
import me.brunofelix.googlecertification.extension.createNotificationChannel
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDebugLog()
        createNotificationChannel()
    }

    private fun initDebugLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
