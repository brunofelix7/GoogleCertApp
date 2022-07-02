package me.brunofelix.googlecertapp.ui

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun initUI()

    abstract fun initObjects()

    abstract fun observeData()
}