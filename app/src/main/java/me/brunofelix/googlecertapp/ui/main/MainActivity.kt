package me.brunofelix.googlecertapp.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.AppDatabase
import me.brunofelix.googlecertapp.data.TaskRepositoryImpl
import me.brunofelix.googlecertapp.ui.BaseActivity
import me.brunofelix.googlecertapp.utils.AppProvider
import me.brunofelix.googlecertapp.utils.JsonReader.getDataFromJson
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initObjects()
        observeData()
        fillDatabase()
    }

    override fun initUI() {

    }

    override fun initObjects() {
        val db = AppDatabase.getInstance(this)
        val dao = db.taskDao()
        val repository = TaskRepositoryImpl(dao)
        val provider = AppProvider(this)
        val factory = MainViewModelFactory(repository, Dispatchers.IO, provider)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.findAll("name").collect {
                Timber.d(it.toString())
            }
        }
    }

    private fun fillDatabase() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        if (prefs.getBoolean(getString(R.string.pref_is_db_empty), true)) {
            prefs.edit().apply {
                putBoolean(getString(R.string.pref_is_db_empty), false)
                apply()
            }
            for (task in getDataFromJson(this)) {
                viewModel.addTask(task)
            }
        } else {
            observeData()
        }
    }
}