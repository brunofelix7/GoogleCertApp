package me.brunofelix.googlecertapp.ui.taskdetails

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.AppDatabase
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskEnum
import me.brunofelix.googlecertapp.data.TaskRepositoryImpl
import me.brunofelix.googlecertapp.databinding.ActivityTaskDetailsBinding
import me.brunofelix.googlecertapp.extensions.snackbar
import me.brunofelix.googlecertapp.extensions.toast
import me.brunofelix.googlecertapp.ui.taskadd.TaskAddViewModel
import me.brunofelix.googlecertapp.ui.taskadd.TaskAddViewModelFactory
import me.brunofelix.googlecertapp.utils.AppConstants
import me.brunofelix.googlecertapp.utils.AppProvider
import timber.log.Timber

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding
    private lateinit var viewModel: TaskDetailsViewModel
    private lateinit var task: Task
    private lateinit var stateAdapter: ArrayAdapter<TaskEnum>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initObjects()
        observeData()
    }

    private fun initUI() {
        setTheme(R.style.ThemeGoogleCertApp)

        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.inflateMenu(R.menu.details_menu)
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.action_delete -> {
                    deleteDialog()
                    true
                }
                else -> false
            }
        }

        stateAdapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, TaskEnum.values())

        binding.autoDropdownState.setAdapter(stateAdapter)

        binding.btnUpdate.setOnClickListener {
            submitForm(it)
        }
    }

    private fun initObjects() {
        val db = AppDatabase.getInstance(this)
        val dao = db.taskDao()
        val repository = TaskRepositoryImpl(dao)
        val provider = AppProvider(this)
        val factory = TaskDetailsViewModelFactory(repository, Dispatchers.IO, provider)

        viewModel = ViewModelProvider(this, factory)[TaskDetailsViewModel::class.java]

        val id = intent.getLongExtra(AppConstants.TASK_ID, 0)
        viewModel.findTaskById(id)
    }

    private fun submitForm(view: View) {
        val taskName = binding.inputName.text.toString()
        val taskState = binding.autoDropdownState.text.toString()

        if (taskName.isEmpty()) {
            view.snackbar(AppConstants.FORM_SUBMIT_ERROR)
            return
        }

        viewModel.updateTask(Task(
            id = task.id,
            name = taskName,
            state = taskState,
            date = System.currentTimeMillis()
        ))
    }

    private fun deleteDialog() {
        val title = getString(R.string.title_dialog_delete)
        val message = String.format(getString(R.string.msg_dialog_delete), task.name)

        val builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(R.string.btn_dialog_yes) { dialog, id ->
                viewModel.deleteTask(task)
            }
            setNegativeButton(R.string.btn_dialog_no) { dialog, id ->
                dialog.dismiss()
            }
        }
        builder.create().apply { show() }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.liveData.observe(this@TaskDetailsActivity) { uiState ->
                when (uiState) {
                    is TaskDetailsUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is TaskDetailsUiState.OnFound -> {
                        binding.progressBar.isVisible = false

                        task = uiState.task
                        binding.inputName.setText(task.name)
                        binding.autoDropdownState.setText(task.state, false)
                    }
                    is TaskDetailsUiState.OnDeleted -> {
                        binding.progressBar.isVisible = false
                        onBackPressed()
                    }
                    is TaskDetailsUiState.OnUpdated -> {
                        binding.progressBar.isVisible = false
                        onBackPressed()
                    }
                    is TaskDetailsUiState.Error -> {
                        binding.progressBar.isVisible = false
                        toast(uiState.message)
                    }
                }
            }
        }
    }
}
