package me.brunofelix.googlecertapp

import android.app.Application
import me.brunofelix.googlecertapp.utils.initDebugLog

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDebugLog()
    }
}
