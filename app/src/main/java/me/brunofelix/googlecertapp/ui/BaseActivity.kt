package me.brunofelix.googlecertapp.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding> constructor(
    private val bindingInflater: (inflater: LayoutInflater) -> T
) : AppCompatActivity() {

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun initUI()

    abstract fun initObjects()

    abstract fun observeData()
}