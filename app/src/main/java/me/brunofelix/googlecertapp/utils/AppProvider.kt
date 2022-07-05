package me.brunofelix.googlecertapp.utils

import android.content.Context
import android.content.res.Resources

class AppProvider constructor(private val context: Context) {

    fun res(): Resources = context.resources

    fun context(): Context = context.applicationContext
}
