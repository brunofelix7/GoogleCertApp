package me.brunofelix.googlecertapp.ui.tasklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.AppDatabase
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskRepositoryImpl
import me.brunofelix.googlecertapp.databinding.ActivityTaskListBinding
import me.brunofelix.googlecertapp.extensions.toast
import me.brunofelix.googlecertapp.ui.tasklist.paging.ItemLoadStateAdapter
import me.brunofelix.googlecertapp.utils.AppProvider
import me.brunofelix.googlecertapp.utils.JsonReader.getDataFromJson
import timber.log.Timber

class TaskListActivity : AppCompatActivity(), TaskListClickListener {

    private lateinit var viewModel: TaskListViewModel
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initObjects()
        observeData()
        fillDatabase()
        adapterConfig()
    }

    private fun initUI() {
        setTheme(R.style.ThemeGoogleCertApp)

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            Timber.d("Fab click")
        }

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.action_sort -> {
                    Timber.d("Sort by item click")
                    true
                }
                R.id.action_settings -> {
                    Timber.d("Settings item click")
                    true
                }
                else -> false
            }
        }
    }

    private fun initObjects() {
        val db = AppDatabase.getInstance(this)
        val dao = db.taskDao()
        val repository = TaskRepositoryImpl(dao)
        val provider = AppProvider(this)
        val factory = TaskListViewModelFactory(repository, Dispatchers.IO, provider)

        adapter = TaskListAdapter()
        viewModel = ViewModelProvider(this, factory)[TaskListViewModel::class.java]
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.findAll("name").collect {
                adapter.submitData(it)
            }
        }
    }

    private fun adapterConfig() {
        adapter.listener = this
        adapter.context = this

        binding.rvSeries.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ItemLoadStateAdapter { adapter.retry() },
            footer = ItemLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.rvSeries.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                toast("\uD83D\uDE28 Oops! ${it.error}")
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

    override fun onTaskClick(task: Task) {
        Timber.d(task.name)
    }
}