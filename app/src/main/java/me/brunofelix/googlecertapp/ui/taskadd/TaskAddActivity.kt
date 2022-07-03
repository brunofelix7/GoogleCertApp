package me.brunofelix.googlecertapp.ui.taskadd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.AppDatabase
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskRepositoryImpl
import me.brunofelix.googlecertapp.databinding.ActivityTaskAddBinding
import me.brunofelix.googlecertapp.extensions.snackbar
import me.brunofelix.googlecertapp.extensions.toast
import me.brunofelix.googlecertapp.utils.AppConstants
import me.brunofelix.googlecertapp.utils.AppProvider

class TaskAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskAddBinding
    private lateinit var viewModel: TaskAddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initObjects()
        observeData()
    }

    private fun initUI() {
        setTheme(R.style.ThemeGoogleCertApp)

        binding = ActivityTaskAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.inflateMenu(R.menu.add_menu)
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            submitForm(it)
        }
    }

    private fun initObjects() {
        val db = AppDatabase.getInstance(this)
        val dao = db.taskDao()
        val repository = TaskRepositoryImpl(dao)
        val provider = AppProvider(this)
        val factory = TaskAddViewModelFactory(repository, Dispatchers.IO, provider)

        viewModel = ViewModelProvider(this, factory)[TaskAddViewModel::class.java]
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.liveData.observe(this@TaskAddActivity) { uiState ->
                when (uiState) {
                    is TaskAddUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is TaskAddUiState.Success -> {
                        binding.progressBar.isVisible = false
                        toast(uiState.message)
                        onBackPressed()
                    }
                    is TaskAddUiState.Error -> {
                        binding.progressBar.isVisible = false
                        toast(uiState.message)
                    }
                }
            }
        }
    }

    private fun submitForm(view: View) {
        val taskName = binding.etName.text.toString()

        if (taskName.isEmpty()) {
            view.snackbar(AppConstants.FORM_SUBMIT_ERROR)
            return
        }
        viewModel.addTask(Task(name = taskName))
    }
}
